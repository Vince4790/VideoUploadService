package com.video.upload.aws;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.video.upload.model.AwsCredential;
import com.video.upload.repository.AwsCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class AwsS3Connector {

    @Autowired
    private AwsCredentialRepository awsCredentialRepository;

    private static final String BUCKET_NAME = "video-upload-vince";
    private static final String S3_REGION_ENDPOINT = "https://s3-ap-southeast-1.amazonaws.com/video-upload-vince";

    public String  pushVideoToS3Bucked(File file) throws Exception {

        AwsCredential credential = awsCredentialRepository.findAll().get(0);
        System.out.println(credential.getAccessKeyId()+ " " + credential.getSecretAccessKey());
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(credential.getAccessKeyId(), credential.getSecretAccessKey());

        AmazonS3Client s3Client = new AmazonS3Client(awsCreds);

        PutObjectRequest putRequest1 = new PutObjectRequest(BUCKET_NAME, file.getName(), file);
        putRequest1.setCannedAcl(CannedAccessControlList.PublicRead);
        PutObjectResult response = s3Client.putObject(putRequest1);
        String url = S3_REGION_ENDPOINT+"/"+file.getName();

        System.out.println("Resource URL: " + url);

        return url;
    }
}

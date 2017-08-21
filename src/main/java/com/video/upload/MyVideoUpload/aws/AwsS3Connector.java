package com.video.upload.MyVideoUpload.aws;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

import java.io.File;

public class AwsS3Connector {
    private static final String BUCKET_NAME = "video-upload-vince";

    public static String  pushVideoToS3Bucked(File file) throws Exception {


        BasicAWSCredentials awsCreds = new BasicAWSCredentials(Credentials.access_key_id, Credentials.secret_access_key);

        AmazonS3Client s3Client = new AmazonS3Client(awsCreds);

        PutObjectRequest putRequest1 = new PutObjectRequest(BUCKET_NAME, file.getName(), file);
        putRequest1.setCannedAcl(CannedAccessControlList.PublicRead);
        PutObjectResult response = s3Client.putObject(putRequest1);
        String url = s3Client.getResourceUrl(BUCKET_NAME,file.getName());

        System.out.println("Resource URL: " + url);

        return url;
    }
}

package com.video.upload.MyVideoUpload.service;

import com.google.common.collect.Lists;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.video.upload.MyVideoUpload.aws.AwsS3Connector;
import com.video.upload.MyVideoUpload.model.User;
import com.video.upload.MyVideoUpload.model.Video;
import com.video.upload.MyVideoUpload.model.VideoRemoveRequest;
import com.video.upload.MyVideoUpload.repository.VideoRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ServerErrorException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {
    private static final String FILE_UPLOAD_STATUS_INCOMPLETE = "Incomplete";
    private static final String FILE_UPLOAD_STATUS_COMPLETE = "Completed";
    @Autowired
    private VideoRepository videoRepository;

    private User getCurrentUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public List<Video> getVideosByCurrentUser() {
        User currentUser = getCurrentUser();
        return videoRepository.findByUserId(currentUser.getId());
    }

    @Override
    public Video addVideoByCurrentUser() {
        Video newVideo = new Video();
        Date currentDate = new Date();
        newVideo.setName("Video3");
        newVideo.setCreatedDate(currentDate);
        newVideo.setLastModifiedDate(currentDate);
        newVideo.setUrl("http://test");
        newVideo.setUserId(getCurrentUser().getId());
        videoRepository.save(newVideo);

        return newVideo;
    }

    @Override
    public void deleteVideosByCurrentUser(int[] ids) {
        List<Long> videoIds = new ArrayList<>();
        for(int i : ids){
            videoIds.add((long) i);
        }
        videoRepository.deleteByIds(videoIds);
    }

    @Override
    public void deleteAllVideosByCurrentUser() {
        Long userId = getCurrentUser().getId();
        videoRepository.deleteAllByUserId(userId);
    }

    @Override
    public void storeChunkTemporary(MultipartFile file, String name, String chunk, String checksum) throws IOException {
        FileOutputStream fos;
        File ofile = new File(name+"_part"+chunk);
        byte[] fileBytes;
        try {
            fos = new FileOutputStream(ofile, true);
            fileBytes = file.getBytes();
            String md5Hex = DigestUtils
                    .md5Hex(fileBytes).toUpperCase();
            System.out.println("Md5:"+ md5Hex);
            validateMD5Checksum(md5Hex, checksum);
            fos.write(fileBytes);
            fos.flush();
            fos.close();
        }catch (Exception exception){
            exception.printStackTrace();
        }

        System.out.println("completed stored chunk, md5");
    }

    private void validateMD5Checksum(String fileChecksume, String requestChecksum) {
        if (!fileChecksume.equalsIgnoreCase(requestChecksum)){
            throw new ServerErrorException("File checksum invalid!!");
        }
    }

    @Override
    public Video uploadNewVideo(String videoName, int chunks, String ext) throws Exception {
        FileOutputStream fos;
        File ofile = new File(videoName+"."+ext);
        fos = new FileOutputStream(ofile,true);
        byte[] fileBytes;
        for (int i=0;i<chunks;i++){
            File chunk = new File(videoName+"_part"+i);
            FileInputStream fin = new FileInputStream(chunk);
            try {
                fos = new FileOutputStream(ofile, true);
                fileBytes = new byte[(int)chunk.length()];
                fin.read(fileBytes);
                fos.write(fileBytes);
                fos.flush();
                fos.close();
                fin.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            } finally{
                chunk.delete();
            }
        }
        System.out.println("completed merge file");

        System.out.println("start upload to S3");

        String url = AwsS3Connector.pushVideoToS3Bucked(ofile);

        System.out.println("done upload to S3");
        System.out.println("save to database");
        Video newVideo = new Video();
        newVideo.setUserId(getCurrentUser().getId());
        newVideo.setUrl(url);
        newVideo.setName(videoName);

        Video created = videoRepository.save(newVideo);
        System.out.println("done save to database");

        return created;
    }

    @Override
    public String validateAllChunksUploaded(String name, int total) {
        for (int i=0;i<total;i++){
            File check = new File(name+"_part"+i);
            if(!check.exists()) {
                return FILE_UPLOAD_STATUS_INCOMPLETE;
            }

        }
        return FILE_UPLOAD_STATUS_COMPLETE;
    }
}

package com.video.upload.MyVideoUpload.service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.video.upload.MyVideoUpload.model.User;
import com.video.upload.MyVideoUpload.model.Video;
import com.video.upload.MyVideoUpload.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {
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
    public List<Video> deleteVideosByCurrentUser() {
        return null;
    }
}

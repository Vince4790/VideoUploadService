package com.video.upload.MyVideoUpload.service;

import com.video.upload.MyVideoUpload.model.Video;
import org.springframework.stereotype.Service;

import java.util.List;

public interface VideoService {
    List<Video> getVideosByCurrentUser();
    Video addVideoByCurrentUser();
    List<Video> deleteVideosByCurrentUser();
}

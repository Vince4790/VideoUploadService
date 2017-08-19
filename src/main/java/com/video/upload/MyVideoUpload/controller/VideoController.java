package com.video.upload.MyVideoUpload.controller;

import com.video.upload.MyVideoUpload.model.Video;
import com.video.upload.MyVideoUpload.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VideoController {
    @Autowired
    private VideoService videoService;

    @RequestMapping(value = "/api/videos", method = RequestMethod.GET)
    public ResponseEntity<List<Video>> getVideos(){
        List<Video> video = videoService.getVideosByCurrentUser();

        return new ResponseEntity<>(video, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/video", method = RequestMethod.POST)
    public ResponseEntity<Video> addVideo(){
        Video videos = videoService.addVideoByCurrentUser();

        return new ResponseEntity<>(videos, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/videos", method = RequestMethod.DELETE)
    public ResponseEntity<HttpStatus> deleteVideos(){
        List<Video> videos = videoService.deleteVideosByCurrentUser();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

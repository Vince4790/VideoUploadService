package com.video.upload.MyVideoUpload.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.video.upload.MyVideoUpload.model.Video;
import com.video.upload.MyVideoUpload.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
public class VideoController {
    @Autowired
    private VideoService videoService;

    @RequestMapping(value = "/api/videos", method = RequestMethod.GET)
    public ResponseEntity<List<Video>> getVideos(){
        List<Video> video = videoService.getVideosByCurrentUser();

        return new ResponseEntity<>(video, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/video/upload", method = RequestMethod.POST)
    public ResponseEntity<Video> uploadNewVideo(@RequestParam String videoName, @RequestParam int chunks, @RequestParam String ext) throws Exception {
        Video created = videoService.uploadNewVideo(videoName, chunks, ext);

        return new ResponseEntity<>(created, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/video", method = RequestMethod.POST)
    public ResponseEntity<String> addVideo(@RequestParam("file") MultipartFile file,@RequestParam("name") String name, @RequestParam("chunk") String chunk,@RequestParam("checksum") String checksum,
                                           @RequestParam("total") int total) throws IOException {
        System.out.println("processing file:" + name + "chunk" + chunk);
        System.out.println("files length:"+ file.getSize());
        videoService.storeChunkTemporary(file, name, chunk, checksum);
        String status = videoService.validateAllChunksUploaded(name, total);
        System.out.println("status:"+ status);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/videos/remove", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> deleteVideos(@RequestParam("ids") String ids){
        Gson gson = new GsonBuilder().create();
        int[] videoIds = gson.fromJson(ids, int[].class);
        videoService.deleteVideosByCurrentUser(videoIds);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/api/videos/remove/all", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> deleteVideos(){
        videoService.deleteAllVideosByCurrentUser();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

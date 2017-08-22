package com.video.upload.MyVideoUpload.service;

import com.video.upload.MyVideoUpload.model.Video;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface VideoService {
    List<Video> getVideosByCurrentUser();
    void deleteVideosByCurrentUser(int[] ids);
    void deleteAllVideosByCurrentUser();

    void storeChunkTemporary(MultipartFile file, String name, String chunk, String checksum) throws IOException;

    Video uploadNewVideo(String videoName, int chunks, String ext) throws Exception;

    String validateAllChunksUploaded(String name, int total);
}

package com.video.upload.MyVideoUpload.repository;

import com.video.upload.MyVideoUpload.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByUserId(Long userId);
}

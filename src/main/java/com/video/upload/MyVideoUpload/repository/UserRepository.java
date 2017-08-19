package com.video.upload.MyVideoUpload.repository;

import com.video.upload.MyVideoUpload.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

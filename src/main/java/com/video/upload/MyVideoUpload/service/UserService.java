package com.video.upload.MyVideoUpload.service;

import com.video.upload.MyVideoUpload.model.User;
import org.springframework.stereotype.Service;


public interface UserService {
    User findByUsername(String username);
    void saveUser(User user);
}

package com.video.upload.MyVideoUpload.service;

import com.video.upload.MyVideoUpload.model.User;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;


public interface UserService {
    User findByUsername(String username);
    User createNewUser(User user) throws ValidationException;
}

package com.video.upload.service;

import com.video.upload.model.User;

import javax.xml.bind.ValidationException;


public interface UserService {
    User findByUsername(String username);
    User createNewUser(User user) throws ValidationException;
    User getCurrentUser();

}

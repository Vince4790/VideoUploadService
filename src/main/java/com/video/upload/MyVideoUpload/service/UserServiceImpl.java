package com.video.upload.MyVideoUpload.service;

import com.video.upload.MyVideoUpload.model.User;
import com.video.upload.MyVideoUpload.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User createNewUser(User user) throws ValidationException {
        User existing = userRepository.findByUsername(user.getUsername());
        if (existing != null){
            throw new ValidationException("User name already exist!");
        }

        user.setCreatedDate(new Date());
        user.setLastModifiedDate(new Date());
        User created = userRepository.save(user);

        return created;
    }

}

package com.video.upload.service;

import com.video.upload.model.User;
import com.video.upload.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Override
    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}

package com.video.upload.controller;

import com.video.upload.model.User;
import com.video.upload.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;

@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public ResponseEntity<User> createUser(@RequestParam String username, @RequestParam String password) throws ValidationException {
        User currentUser = userService.createNewUser(new User(username, password));
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }
}

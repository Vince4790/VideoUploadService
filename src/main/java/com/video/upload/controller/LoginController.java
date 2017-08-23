package com.video.upload.controller;

import com.video.upload.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class LoginController {
        @RequestMapping(value = "/login", method = RequestMethod.POST)
        public ResponseEntity<User> login() {
                User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                return new ResponseEntity<>(currentUser, HttpStatus.OK);
        }
}

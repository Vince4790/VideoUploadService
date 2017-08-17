package com.video.upload.MyVideoUpload.controller;

import com.video.upload.MyVideoUpload.service.LoginService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    private LoginService loginService;

    @RequestMapping(value = "/greeting", method = RequestMethod.GET)
    public String login(){
        return "hello";
    }
}

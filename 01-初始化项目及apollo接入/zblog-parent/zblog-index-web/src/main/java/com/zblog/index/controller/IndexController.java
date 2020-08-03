package com.zblog.index.controller;

import com.zblog.basics.service.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
    @Reference
    UserService userService;

    @RequestMapping("getUsername")
    @ResponseBody
    public String getUsername(){
        System.out.println("userService.getUserName():"+userService.getUserName());
        return userService.getUserName();
    }
}

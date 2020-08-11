package com.zblog.index.controller;

import com.zblog.basics.service.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Reference
    UserService userService;

    @RequestMapping("getUsername")
    @ResponseBody
    public String getUsername() throws Exception {
        String userName = userService.getUserName();
        System.out.println("userService.getUserName():"+userName);
        return userName;
    }
}

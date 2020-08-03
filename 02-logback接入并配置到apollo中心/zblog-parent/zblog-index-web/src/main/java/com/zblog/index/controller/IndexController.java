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
    public String getUsername(){
        System.out.println("userService.getUserName():"+userService.getUserName());
        logger.error("userService.getUserName():"+userService.getUserName());
        logger.info("userService.getUserName():"+userService.getUserName());
        logger.debug("userService.getUserName():"+userService.getUserName());
        logger.trace("userService.getUserName():"+userService.getUserName());
        return userService.getUserName();
    }
}

package com.zblog.basics.service.impl;

import com.zblog.basics.service.UserService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class UserServiceImpl implements UserService {

    @Value("${userNameTest}")
    private String userName;

    @Override
    public String getUserName() {
        return userName;
    }
}

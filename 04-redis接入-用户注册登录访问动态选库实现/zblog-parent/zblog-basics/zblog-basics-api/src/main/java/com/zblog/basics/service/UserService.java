package com.zblog.basics.service;

import com.zblog.basics.vo.AccountProfile;
import com.zblog.basics.vo.UserVO;

public interface UserService {
    /**
     *  test
     * @return
     * @throws Exception
     */
    String getUserName(String username,String signDb) throws Exception;

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    AccountProfile login(String username, String password);

    /**
     * 登录,用于记住登录时获取用户信息
     * @param id
     * @return
     */
    AccountProfile findProfile(Long id);

    /**
     * 注册
     * @param user
     */
    UserVO register(UserVO user);
}

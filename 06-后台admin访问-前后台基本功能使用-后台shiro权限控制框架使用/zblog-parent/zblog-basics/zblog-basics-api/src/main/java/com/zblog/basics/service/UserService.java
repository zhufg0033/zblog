package com.zblog.basics.service;

import com.zblog.sharedb.vo.AccountProfile;
import com.zblog.sharedb.vo.DataPageVO;
import com.zblog.sharedb.vo.PageModuls;
import com.zblog.sharedb.vo.UserVO;
import com.zblog.util.conf.Result;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;

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
    Result<AccountProfile> login(String username, String password);

    /**
     * 登录,用于记住登录时获取用户信息
     * @param id
     * @return
     */
    Result<AccountProfile> findProfile(Long id);

    /**
     * 注册
     * @param user
     */
    UserVO register(UserVO user);

    /**
     * 查询单个用户
     * @param userId
     * @return
     */
    @Cacheable(key = "#userId")
    UserVO get(long userId);

    UserVO getByUsername(String username);

    UserVO getByEmail(String email);

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    @CacheEvict(key = "#user.getId()")
    AccountProfile update(UserVO user);

    /**
     * 修改用户信息
     * @param email
     * @return
     */
    @CacheEvict(key = "#id")
    AccountProfile updateEmail(long id, String email);

    /**
     * 修改头像
     * @param id
     * @param path
     * @return
     */
    @CacheEvict(key = "#id")
    AccountProfile updateAvatar(long id, String path);

    /**
     * 修改密码
     * @param id
     * @param newPassword
     */
    void updatePassword(long id, String newPassword);

    /**
     * 修改密码
     * @param id
     * @param oldPassword
     * @param newPassword
     */
    void updatePassword(long id, String oldPassword, String newPassword);

    /**
     * 修改用户状态
     * @param id
     * @param status
     */
    void updateStatus(long id, int status);

    long count();

    DataPageVO<UserVO> paging(PageModuls pageModuls, String name);
}

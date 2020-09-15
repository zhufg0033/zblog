package com.zblog.basics.service.impl;


import com.github.pagehelper.PageInfo;
import com.zblog.basics.service.UserService;
import com.zblog.basics.service.commpont.Test1Commpont;
import com.zblog.basics.service.commpont.Test2Commpont;
import com.zblog.basics.utils.BasicsBeanMapUtils;
import com.zblog.sharedb.dao.subtreasury.MtoMessageDao;
import com.zblog.sharedb.dao.subtreasury.MtoUserDao;
import com.zblog.sharedb.entity.MtoUser;
import com.zblog.sharedb.vo.*;
import com.zblog.util.conf.Result;
import com.zblog.util.exception.MtonsException;
import com.zblog.util.lang.CacheConsts;
import com.zblog.util.lang.Consts;
import com.zblog.util.lang.EntityStatus;
import com.zblog.util.sharedb.DynamicDataSourceContextHolder;
import com.zblog.util.tool.MD5;
import com.zblog.util.tool.PageHelpers;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${userNameTest}")
    private String userName;

    @Autowired
    private Test1Commpont test1Commpont;

    @Autowired
    private Test2Commpont test2Commpont;

    @Autowired
    private MtoUserDao mtoUserDao;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private MtoMessageDao mtoMessageDao;

    @Override
    public String getUserName(String username,String signDb) throws Exception{
//        MtoUser byUsername = mtoUserDao.findByUsername(username);

        return this.userName;
    }

    @Override
    @Transactional
    public Result<AccountProfile> login(String username, String password) {


        String utoken = (String)redisTemplate.opsForHash().get(CacheConsts.utoken, username);
        if(StringUtils.isBlank(utoken)){
            log.error("用户["+username+"]在缓存中不存在");
            return Result.failure("用户或者密码错误");
        }

        DynamicDataSourceContextHolder.setDataSourceKey(utoken);

        MtoUser mtoUser = mtoUserDao.findByUsername(username);

        if(null == mtoUser){
            log.error("用户["+username+"]在数据库中不存在");
            return Result.failure("用户或者密码错误");
        }

        if(!StringUtils.equals(mtoUser.getPassword(), password)){
            log.error("用户["+username+"]密码认证失败");
            return Result.failure("用户或者密码错误");
        }

        mtoUser.setLastLogin(Calendar.getInstance().getTime());
        mtoUserDao.updateByPrimaryKey(mtoUser);//保存最后登录时间

        AccountProfile accountProfile = BasicsBeanMapUtils.copyPassport(mtoUser);

        BadgesCount badgesCount = new BadgesCount();
        badgesCount.setMessages(mtoMessageDao.countByUserIdAndStatus(mtoUser.getId(), Consts.UNREAD));
        accountProfile.setBadgesCount(badgesCount);

        return Result.success(accountProfile);
    }

    @Override
    public Result<AccountProfile> findProfile(Long id) {
        MtoUser mtoUser = mtoUserDao.selectByPrimaryKey(id);
        AccountProfile accountProfile = BasicsBeanMapUtils.copyPassport(mtoUser);
        return Result.success(accountProfile);
    }

    @Override
    @Transactional
    public UserVO register(UserVO user) {

        Assert.notNull(user, "Parameter user can not be null!");

        Assert.hasLength(user.getUsername(), "用户名不能为空!");
        Assert.hasLength(user.getPassword(), "密码不能为空!");

        String utoken = (String)redisTemplate.opsForHash().get(CacheConsts.utoken, user.getUsername());
        Assert.isNull(utoken,"用户名已经存在！");

        if(StringUtils.isNotBlank(user.getEmail())) {
            utoken = (String) redisTemplate.opsForHash().get(CacheConsts.utoken, user.getEmail());
            Assert.isNull(utoken, "邮箱已经存在！");
        }

        MtoUser mtoUser = new MtoUser();
        BeanUtils.copyProperties(user,mtoUser);

        if(StringUtils.isBlank(mtoUser.getName())){
            mtoUser.setName(user.getUsername());
        }

        Date now = Calendar.getInstance().getTime();
        mtoUser.setPassword(MD5.md5(mtoUser.getPassword()));
        mtoUser.setStatus(EntityStatus.ENABLED);
        if(mtoUser.getGender()  == null) {
            mtoUser.setGender(1);
        }
        mtoUser.setCreated(now);

        //获取当前年份，进行切库
        /**
         * 这里我使用的是时间切库，使用hash值 对 偶数值去余分库会更好   数据更加均衡
         */
        String sign = "zblog"+Calendar.getInstance().get(Calendar.YEAR)+"";
        mtoUser.setSignDb(sign);

        DynamicDataSourceContextHolder.setDataSourceKey(sign);
        mtoUserDao.insert(mtoUser);

        if(StringUtils.isNotBlank(mtoUser.getUsername())) {
            redisTemplate.opsForHash().put(CacheConsts.utoken, mtoUser.getUsername(), sign);
        }
        if(StringUtils.isNotBlank(mtoUser.getEmail())){
            redisTemplate.opsForHash().put(CacheConsts.utoken, mtoUser.getUsername(), sign);
        }

        return BasicsBeanMapUtils.copy(mtoUser);
    }

    @Override
    public UserVO get(long userId) {
        MtoUser mtoUser = mtoUserDao.selectByPrimaryKey(userId);
        if (mtoUser != null) {
            return BasicsBeanMapUtils.copy(mtoUser);
        }
        return null;
    }

    @Override
    public UserVO getByUsername(String username) {
        return BasicsBeanMapUtils.copy(mtoUserDao.findByUsername(username));
    }

    @Override
    public UserVO getByEmail(String email) {
        return BasicsBeanMapUtils.copy(mtoUserDao.findByEmail(email));
    }

    @Override
    @Transactional
    public AccountProfile update(UserVO user) {
        MtoUser po = mtoUserDao.selectByPrimaryKey(user.getId());
        po.setName(user.getName());
        po.setSignature(user.getSignature());
        mtoUserDao.updateByPrimaryKey(po);
        return BasicsBeanMapUtils.copyPassport(po);
    }

    @Override
    @Transactional
    public AccountProfile updateEmail(long id, String email) {
        MtoUser po = mtoUserDao.selectByPrimaryKey(id);

        if (email.equals(po.getEmail())) {
            throw new MtonsException("邮箱地址没做更改");
        }

        MtoUser check = mtoUserDao.findByEmail(email);

        if (check != null && check.getId() != po.getId()) {
            throw new MtonsException("该邮箱地址已经被使用了");
        }
        po.setEmail(email);
        mtoUserDao.updateByPrimaryKey(po);
        return BasicsBeanMapUtils.copyPassport(po);
    }

    @Override
    @Transactional
    public AccountProfile updateAvatar(long id, String path) {
        MtoUser po = mtoUserDao.selectByPrimaryKey(id);
        po.setAvatar(path);
        mtoUserDao.updateByPrimaryKey(po);
        return BasicsBeanMapUtils.copyPassport(po);
    }

    @Override
    @Transactional
    public void updatePassword(long id, String newPassword) {
        MtoUser po = mtoUserDao.selectByPrimaryKey(id);

        Assert.hasLength(newPassword, "密码不能为空!");

        po.setPassword(MD5.md5(newPassword));
        mtoUserDao.updateByPrimaryKey(po);
    }

    @Override
    @Transactional
    public void updatePassword(long id, String oldPassword, String newPassword) {
        MtoUser po = mtoUserDao.selectByPrimaryKey(id);

        Assert.hasLength(newPassword, "密码不能为空!");

        Assert.isTrue(MD5.md5(oldPassword).equals(po.getPassword()), "当前密码不正确");
        po.setPassword(MD5.md5(newPassword));
        mtoUserDao.updateByPrimaryKey(po);
    }

    @Override
    @Transactional
    public void updateStatus(long id, int status) {
        MtoUser po = mtoUserDao.selectByPrimaryKey(id);

        po.setStatus(status);
        mtoUserDao.updateByPrimaryKey(po);
    }

    @Override
    public long count() {
        return mtoUserDao.count();
    }

    @Override
    public DataPageVO<UserVO> paging(PageModuls pageModuls, String name) {
        pageModuls.setSort("id desc");
        PageHelpers.startPage(pageModuls);
        List<MtoUser> page = mtoUserDao.findLikeUsername(name == null ? null : "%" + name + "%");

        List<UserVO> rets = new ArrayList<>();
        if(page != null){

            page.forEach(n -> rets.add(BasicsBeanMapUtils.copy(n)));
            PageInfo<MtoUser> p = new PageInfo<>(page);

            return new DataPageVO<>(pageModuls,rets,p.getTotal());
        }

        return new DataPageVO<>(pageModuls,rets,0);


    }

}

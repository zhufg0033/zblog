package com.zblog.basics.service.impl;


import com.zblog.basics.service.UserService;
import com.zblog.basics.service.commpont.Test1Commpont;
import com.zblog.basics.service.commpont.Test2Commpont;
import com.zblog.basics.utils.BasicsBeanMapUtils;
import com.zblog.sharedb.dao.MtoUserDao;
import com.zblog.sharedb.entity.MtoUser;
import com.zblog.sharedb.vo.AccountProfile;
import com.zblog.sharedb.vo.BadgesCount;
import com.zblog.sharedb.vo.UserVO;
import com.zblog.util.lang.CacheConsts;
import com.zblog.util.lang.EntityStatus;
import com.zblog.util.sharedb.DynamicDataSourceContextHolder;
import com.zblog.util.tool.MD5;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.Date;

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

    @Override
    public String getUserName(String username,String signDb) throws Exception{
//        MtoUser byUsername = mtoUserDao.findByUsername(username);

        return this.userName;
    }

    @Override
    @Transactional
    public AccountProfile login(String username, String password) {

        String utoken = (String)redisTemplate.opsForHash().get(CacheConsts.utoken, username);
        Assert.notNull(utoken,"用户名不存在存在！");
        DynamicDataSourceContextHolder.setDataSourceKey(utoken);

        MtoUser mtoUser = mtoUserDao.findByUsername(username);

        if(null == mtoUser){
            return null;
        }

        Assert.state(StringUtils.equals(mtoUser.getPassword(), password), "密码错误");

        mtoUser.setLastLogin(Calendar.getInstance().getTime());
        mtoUserDao.updateByPrimaryKey(mtoUser);//保存最后登录时间

        AccountProfile accountProfile = BasicsBeanMapUtils.copyPassport(mtoUser);

        BadgesCount badgesCount = new BadgesCount();
        // TODO 需要后续实现
        badgesCount.setMessages(0);

        accountProfile.setBadgesCount(badgesCount);
        return accountProfile;
    }

    @Override
    public AccountProfile findProfile(Long id) {
        return null;
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
        mtoUser.setGender(1);//TODO 先填着
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

}

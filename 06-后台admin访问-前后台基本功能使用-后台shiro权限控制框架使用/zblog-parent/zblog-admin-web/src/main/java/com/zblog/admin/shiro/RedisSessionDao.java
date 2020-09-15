package com.zblog.admin.shiro;

import com.qiniu.util.Base64;
import com.zblog.util.lang.CacheConsts;
import com.zblog.util.tool.JSONUtil;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.*;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName : RedisSessionDao  //类名
 * @Description : shiroRedis保存  //描述
 * @Author : zhufugao  //作者
 * @Date: 2020-09-04 09:37  //时间
 */
public class RedisSessionDao extends AbstractSessionDAO {

    private static Logger logger = LoggerFactory.getLogger(RedisSessionDao.class);

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private ConcurrentHashMap<String,Session> sessionMap = new ConcurrentHashMap<>();

    /**
     * 单位minutes
     */
//    private Long sessionTimeout = 30*24*60L;//30天有效期
    @Value("${sessionTimeout:60}")
    private Long sessionTimeout = 60L;

    public void setSessionTimeout(Long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    private String getKey(String key) {
        return CacheConsts.shiroSession + key;
    }

    private void saveSession(Session session) throws UnknownSessionException {
        if (session != null && session.getId() != null) {
            String key = this.getKey(session.getId().toString());
            session.setTimeout(this.sessionTimeout * 60 * 1000);
            byte[] bytes = SerializationUtils.serialize((Serializable) session);
            String str = Base64.encodeToString(bytes, Base64.DEFAULT);
            BoundValueOperations<String, String> sessionValueOperations = redisTemplate.boundValueOps(key );
            sessionValueOperations.set(str);
            sessionValueOperations.expire(this.sessionTimeout*60*1000, TimeUnit.SECONDS);
            sessionMap.put(key,session);

//            redisTemplate.opsForValue().set(key, JSONUtil.toJSONString(session), this.sessionTimeout*60*1000, TimeUnit.SECONDS);
        } else {
            logger.error("session or session id is null");
        }
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        logger.debug("创建seesion,id=[{}]", session.getId() != null ? session.getId().toString() : "null");
        // 当有游客进入或者remeberme都会调用
        this.saveSession(session);

        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        logger.debug("获取seesion,id=[{}]", sessionId.toString());
        Session readSession = null;
        try {
            if(!sessionMap.containsKey(getKey(sessionId.toString()))){
                Session session = sessionMap.get(getKey(sessionId.toString()));
                Date startTimestamp = session.getStartTimestamp();
                if(new Date().getTime() - startTimestamp.getTime() <= this.sessionTimeout * 60 * 1000){
                    return sessionMap.get(getKey(sessionId.toString()));
                }else{
                    sessionMap.remove(getKey(sessionId.toString()));
                }
            }
            BoundValueOperations<String, String> sessionValueOperations = redisTemplate.boundValueOps(getKey(sessionId.toString()));
            Object obj = sessionValueOperations.get();
            if(obj != null) {
                byte[] b = Base64.decode((String)obj,Base64.DEFAULT);
                readSession = (SimpleSession)SerializationUtils.deserialize(b);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return readSession;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        logger.debug("更新seesion,id=[{}]", session.getId() != null ? session.getId().toString() : "null");
        this.saveSession(session);
    }

    @Override
    public void delete(Session session) {
        logger.debug("删除seesion,id=[{}]", session.getId().toString());
        try {
            String key = getKey(session.getId().toString());
            redisTemplate.delete(key);
            sessionMap.remove(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public Collection<Session> getActiveSessions() {
        logger.info("获取存活的session");

        Set<Session> sessions = new HashSet<>();
        Set<String> keys = redisTemplate.keys(getKey("*"));
        if (keys != null && keys.size() > 0) {
            for (String key : keys) {
                if(key.startsWith(CacheConsts.shiroSession)) {
                    BoundValueOperations<String, String> sessionValueOperations = redisTemplate.boundValueOps(key);
                    Object obj = sessionValueOperations.get();
                    if(obj != null) {
                        byte[] b = Base64.decode((String)obj,Base64.DEFAULT);
                        Session session = (SimpleSession)SerializationUtils.deserialize(b);
                        sessions.add(session);
                    }
                }
            }
        }
        return sessions;
    }

    // 把session对象转化为byte保存到redis中
    public byte[] sessionToByte(Session session){
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        byte[] bytes = null;
        try {
            ObjectOutput oo = new ObjectOutputStream(bo);
            oo.writeObject(session);
            bytes = bo.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    // 把byte还原为session
    public Session byteToSession(byte[] bytes){
        ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
        ObjectInputStream in;
        SimpleSession session = null;
        try {
            in = new ObjectInputStream(bi);
            session = (SimpleSession) in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return session;
    }

}

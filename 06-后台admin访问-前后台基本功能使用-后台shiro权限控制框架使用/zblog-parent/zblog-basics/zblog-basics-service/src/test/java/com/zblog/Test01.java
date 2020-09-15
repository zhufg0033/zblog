package com.zblog;


import com.qiniu.util.Base64;
import com.sun.media.jfxmedia.track.Track;
import com.sun.tools.javac.util.Convert;
import com.zblog.sharedb.entity.MtoUser;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.postgresql.core.Encoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test01 {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;


    public static void main(String[] args) {
        String s = "123456";
        byte[] serialize = SerializationUtils.serialize((Serializable) s);
        String str = Base64.encodeToString(serialize, Base64.DEFAULT);
        byte[] serialize2 = Base64.decode(str,Base64.DEFAULT);


        String deserialize0 = (String)SerializationUtils.deserialize(serialize);
        String deserialize = (String)SerializationUtils.deserialize(serialize2);

    }

    @Test
    public void testStringSetKey() {

        String s = "123456";
        byte[] serialize = SerializationUtils.serialize((Serializable) s);
//        String multStr = Convert.

        redisTemplate.opsForValue().set("777777777", serialize);

        Object o = redisTemplate.opsForValue().get("777777777");
//        ByteArrayInputStream byteArrayOutputStream = new ByteArrayInputStream();
//        byteArrayOutputStream.
        String str = (String)o;



        byte[] serialize2 = str.getBytes();


        String deserialize0 = (String)SerializationUtils.deserialize(serialize);
        String deserialize = (String)SerializationUtils.deserialize(serialize2);



//        stringRedisTemplate.opsForValue().set("yunai", "shuai2");
//
//        System.out.println("-----------");
//
//        System.out.println(stringRedisTemplate.opsForValue().get("yunai"));

    }

    @Test
    public void testStringSetKey02() {

        redisTemplate.opsForValue().set("yunai", "shuai");

        System.out.println("-----------");

        System.out.println(stringRedisTemplate.opsForValue().get("yunai"));


    }

    @Test
    public void testSetAdd() {

        stringRedisTemplate.opsForSet().add("yunai_descriptions", "shuai", "cai");
    }

    @Test
    public void testStringSetKeyUserCache() {
//        UserCacheObject object = new UserCacheObject()
//                .setId(1)
//                .setName("芋道源码")
//                .setGender(1); // 男
//        String key = String.format("user:%d", object.getId());
//        redisTemplate.opsForValue().set(key, object);
        MtoUser mtoUser = new MtoUser();
        mtoUser.setId(2L);
        mtoUser.setName("kkkk");

        String key = String.format("user:%d", mtoUser.getId());
        redisTemplate.opsForValue().set(key, mtoUser);
    }

    @Test
    public void testStringGetKeyUserCache() {
        String key = String.format("user:%d", 2);
        Object value = redisTemplate.opsForValue().get(key);
        System.out.println(value);
    }

}

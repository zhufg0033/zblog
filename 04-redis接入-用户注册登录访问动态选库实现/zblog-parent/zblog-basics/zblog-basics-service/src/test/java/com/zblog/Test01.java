package com.zblog;


import com.zblog.sharedb.entity.MtoUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test01 {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testStringSetKey() {

        stringRedisTemplate.opsForValue().set("yunai", "shuai2");

        System.out.println("-----------");

        System.out.println(stringRedisTemplate.opsForValue().get("yunai"));

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

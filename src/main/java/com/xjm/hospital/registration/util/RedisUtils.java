package com.xjm.hospital.registration.util;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis工具类
 *
 * @author xiangjunming
 * @date 2022/10/16
 */
@Component
public class RedisUtils {

    public static final int DEFAULT_TIMEOUT = 2 * 60 * 60;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 删除精确key值下所有值
     */
    public void deleteKey(String key) {
        redisTemplate.opsForHash().getOperations().delete(key);
    }

    /**
     * 删除key前缀值下所有值
     */
    public void deleteLikeKeys(String key) {
        //最后一定要带上 *
        Set<String> keys = redisTemplate.keys(key + "*");
        if (ObjectUtil.isNotEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 删出key 这里跟下边deleteKey（）最底层实现都是一样的，应该可以通用
     */
    public void delete(String key) {
        redisTemplate.opsForValue().getOperations().delete(key);
    }

    /**
     * 字符串获取值
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 字符串存入值 默认过期时间为2小时
     */
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value, DEFAULT_TIMEOUT, TimeUnit.SECONDS);
    }

    /**
     * 字符串存入值
     */
    public void set(String key, String value, Integer expire) {
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    /**
     * 添加单个
     */
    public void hset(String key, String filed, Object domain, Integer expire) {
        redisTemplate.opsForHash().put(key, filed, domain);
        redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    /**
     * 判断key和field下是否有值
     */
    public Boolean hasKey(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    /**
     * 判断key下是否有值
     */
    public Boolean hasKey(String key) {
        return redisTemplate.opsForHash().getOperations().hasKey(key);
    }

    /**
     * 查询key和field所确定的值
     */
    public Object hasGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    /**
     * 查询该key下所有值
     */
    public Object hasGet(String key) {
        return redisTemplate.opsForHash().entries(key);
    }


}

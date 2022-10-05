package com.xjm.hospital.registration.util;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Description: redis工具类
 *
 * @author: gremlin
 * Date: 2022/6/10 14:43
 * @version: 1.0.0
 */
@Component
public class RedisUtils {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Schema(name = "validTime",description = "reids 中用户信息吗存储2小时 刷新时间")
    private final int validTime = 2;

    @Schema(name = "expirationSeconds",description = "过期时间 秒")
    private final int expirationSeconds = 300;

    /**
     * 删除精确key值下所有值
     */
    public void deleteKey(String key) {
        redisTemplate.opsForHash().getOperations().delete(key);
    }

    /**
     * 删除key前缀值下所有值
     */
    public void deleteKeys(String key) {
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
        redisTemplate.opsForValue().set(key, value, 7200, TimeUnit.SECONDS);
    }

    /**
     * 字符串存入值
     */
    public void set(String key, String value, Integer expire) {
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }


    /**
     * 查询token下的刷新时间
     */
    public Object getTokenValidTimeByToken(String token) {
        return redisTemplate.opsForHash().get(token, "tokenValidTime");
    }

    /**
     * 查询token下的刷新时间
     *
     * @param token 查询的key
     * @return HV
     */
    public Object getUsernameByToken(String token) {
        return redisTemplate.opsForHash().get(token, "username");
    }

    /**
     * 查询token下的刷新时间
     *
     * @param token 查询的key
     * @return HV
     */
    public Object getIpByToken(String token) {
        return redisTemplate.opsForHash().get(token, "ip");
    }

    public void setTokenRefresh(String username, String token, String ip) {
        //刷新时间
        Integer expire = validTime * 60 * 60 * 1000;
        hset(username, "tokenValidTime", DateUtil.getAddDayTime(validTime), expire);
        hset(username, "expirationTime", DateUtil.getAddDaySecond(expirationSeconds), expire);
        hset(username, "username", username, expire);
        hset(username, "token", token, expire);
        hset(username, "ip", ip, expire);
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

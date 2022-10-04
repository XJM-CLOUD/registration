package com.xjm.hospital.registration.util;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

/**
 * Description: token响应类 获取token
 *
 * @author: gremlin
 * Date: 2022/6/10 10:55
 * @version: 1.0.0
 */
@Service
public class TokenResponse {

    @Autowired
    private RedisUtils redisUtil;

    public void getResponse(HttpServletResponse response) {
        //获取用户登录信息
        UserDetails user = RedisUtils.getUser();
        // 获取Token
        Object token = redisUtil.get(user.getUsername() + "token");
        if (StringUtils.isNotBlank((String) token)) {
            response.reset();
            response.setHeader("token", String.valueOf(token));
        } else {
            response.reset();
            response.setHeader("token", String.valueOf(token));
        }
    }
}

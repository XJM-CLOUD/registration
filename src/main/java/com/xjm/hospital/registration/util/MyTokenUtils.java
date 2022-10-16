package com.xjm.hospital.registration.util;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import com.xjm.hospital.registration.security.vo.MyUserDetails;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * JWT保证无状态机制的使用
 *
 * @author xiangjunming
 * @date 2022/10/16
 */
@Slf4j
public class MyTokenUtils {

    @Schema(name = "TOKEN_SIGN_KEY", description = "令牌签名密钥")
    private static final String TOKEN_SIGN_KEY = "xiangjunming";

    @Schema(name = "ACCESS_TOKEN_EXPIRATION_TIME", description = "短令牌过期时间:10分钟")
    public static final long ACCESS_TOKEN_EXPIRATION_TIME = 10 * 60 * 1000;

    @Schema(name = "REFRESH_TOKEN_EXPIRATION_TIME", description = "长令牌过期时间:3天")
    public static final long REFRESH_TOKEN_EXPIRATION_TIME = 3 * 24 * 60 * 60 * 1000;

    /**
     * 生成短令牌token，用于访问业务接口
     */
    public static String generateAccessToken(String subject, Map<String, Object> payloads) {
        JWT jwt = JWT.create().setSubject(subject).setPayload("TokenType", "accessToken");
        payloads.forEach(jwt::setPayload);
        return jwt.setIssuedAt(new Date())
                .setExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .setKey(TOKEN_SIGN_KEY.getBytes()).sign();
    }

    /**
     * 生成刷新令牌refreshToken，用于续签短令牌
     */
    public static String generateRefreshToken(String subject, Map<String, Object> payloads) {
        JWT jwt = JWT.create().setSubject(subject).setPayload("TokenType", "refreshToken");;
        payloads.forEach(jwt::setPayload);
        return jwt.setIssuedAt(new Date())
                .setExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .setKey(TOKEN_SIGN_KEY.getBytes()).sign();
    }

    /**
     * 判断令牌是否accessToken
     */
    public static Boolean isAccessToken(String token) {
        try {
            return ObjectUtil.equal(getPayload(token, "TokenType"), "accessToken");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断令牌是否过期
     */
    public static Boolean validate(String token) {
        try {
            return JWT.of(token).setKey(TOKEN_SIGN_KEY.getBytes()).validate(0);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 解析token,获得subject中的信息
     */
    public static String getSubject() {
        return getSubject(getCurrentRequestToken());
    }
    public static String getSubject(String token) {
        return getPayload(token, "sub");
    }

    /**
     * 获取token自定义属
     */
    public static String getPayload(String token, String payload) {
        return StrUtil.toStringOrNull(JWT.of(token).getPayload(payload));
    }


    /**
     * 根据Bearer认证标准从用户请求中获取token <a href=“https://datatracker.ietf.org/doc/html/rfc6750”></a>
     * @return
     */
    public static String getCurrentRequestToken()  {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authorization = request.getHeader("Authorization");
        String token = null;

        //请求头
        if(StrUtil.isNotBlank(authorization)) {
            List<String> authorizationInfos = StrUtil.splitTrim(authorization, StrUtil.SPACE);
            if(CollUtil.size(authorizationInfos) == 2 && StrUtil.equals(authorizationInfos.get(0), "Bearer")) {
                token = authorizationInfos.get(1);
            }
        } else if(StrUtil.equalsIgnoreCase(request.getMethod(),"GET") || (StrUtil.equalsIgnoreCase(request.getMethod(),"POST")
                && StrUtil.containsIgnoreCase(request.getHeader("Content-Type"),"application/x-www-form-urlencoded"))) {
            token = request.getParameter("access_token");
        }

        return Optional.ofNullable(token)
                .orElse(null);
    }

    /**
     * 获取用户登录信息
     */
    public static MyUserDetails getCurrentLoginUser() {
        // 获取当前用户
        MyUserDetails userDetails
                = (MyUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userDetails;
    }
}

package com.xjm.hospital.registration.security.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.xjm.hospital.registration.exception.ErrorCodeException;
import com.xjm.hospital.registration.resp.ErrorCodeEnum;
import com.xjm.hospital.registration.security.service.TokenService;
import com.xjm.hospital.registration.security.vo.MyUserDetails;
import com.xjm.hospital.registration.util.AccessAddressUtils;
import com.xjm.hospital.registration.util.MyTokenUtils;
import com.xjm.hospital.registration.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    @Resource
    private RedisUtils redisUtil;

    @Resource
    private UserDetailsService userDetailsService;

    @Override
    public Map<String, Object> generateToken(MyUserDetails userDetails, Map<String, Object> payloads) {
        String username = userDetails.getUsername();
        // 生成访问令牌(短令牌)access_token
        String accessToken = MyTokenUtils.generateAccessToken(username, payloads);
        // 生成刷新令牌(长令牌)refresh_token
        String refreshToken = MyTokenUtils.generateRefreshToken(username, payloads);
        // 根据长令牌生成MD5摘要, 存入redis，转变为有状态token
        String refreshTokenMD5Str = DigestUtil.md5Hex(refreshToken);
        redisUtil.set(username + "_token", refreshTokenMD5Str,
                Convert.toInt(MyTokenUtils.REFRESH_TOKEN_EXPIRATION_TIME / 1000));
        log.info("保存token到redis, key:{}, value:{}", username + "_token", refreshTokenMD5Str);

        userDetails.setAccessToken(accessToken);
        userDetails.setRefreshToken(refreshToken);

        // 获取IP地址
        String ip = AccessAddressUtils.getIpAddress();
        HashMap<String, Object> result = MapUtil.of("ip", ip);
        result.put("username", username);
        result.put("access_token", accessToken);
        result.put("refresh_token", refreshToken);
        List<String> roles = StrUtil.split(userDetails.getRoles(), StrUtil.C_COMMA);
        result.put("roles", roles);
        return result;
    }

    @Override
    public Map<String, Object> refreshToken(String refreshToken) {

        if (MyTokenUtils.validate(refreshToken)) {
            // 刷新令牌有效
            String refreshTokenMD5Str = DigestUtil.md5Hex(refreshToken);
            String username = MyTokenUtils.getSubject(refreshToken);
            String userToken = Convert.toStr(redisUtil.get(username + "_token"));
            if (StrUtil.equalsIgnoreCase(userToken, refreshTokenMD5Str)) {
                log.info("刷新令牌校验成功，服务器刷新令牌与请求刷新令牌一致: {}", userToken);
                // 刷新令牌校验成功，服务器刷新令牌与请求刷新令牌一致
                MyUserDetails userDetails = (MyUserDetails) userDetailsService.loadUserByUsername(username);
                // 获取IP地址
                String ip = AccessAddressUtils.getIpAddress();
                Map<String, Object> payloads = MapUtil.of("ip", ip);
                return generateToken(userDetails, payloads);
            } else {
                throw new ErrorCodeException(ErrorCodeEnum.REFRESH_TOKEN_IS_OVERDUE);
            }
        } else {
            throw new ErrorCodeException(ErrorCodeEnum.REFRESH_TOKEN_IS_OVERDUE);
        }
    }
}

package com.xjm.hospital.registration.security.service;

import com.xjm.hospital.registration.security.vo.MyUserDetails;

import java.util.Map;

/**
 * 生成token
 *
 * @author xiangjunming
 * @date 2022/10/16
 */
public interface TokenService {

    /**
     * 生成token
     * @param userDetails
     * @param payloads
     * @return
     */
    Map<String, Object> generateToken(MyUserDetails userDetails, Map<String, Object> payloads);

    /**
     * 刷新token
     * @param refreshToken
     * @return
     */
    Map<String, Object> refreshToken(String refreshToken);
}

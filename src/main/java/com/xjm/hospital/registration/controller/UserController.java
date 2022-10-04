package com.xjm.hospital.registration.controller;

import com.xjm.hospital.registration.resp.ResponseResult;
import com.xjm.hospital.registration.security.UserDetailsServiceImpl;
import com.xjm.hospital.registration.util.JwtTokenUtils;
import com.xjm.hospital.registration.util.RequestGetTokenUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserDetailsServiceImpl userDetailsService;

    @RequestMapping("getInfo")
    public ResponseResult<UserDetails> getInfo(HttpServletRequest request) {
        String authToken = RequestGetTokenUtil.getToken(request);
        String username = JwtTokenUtils.parseToken(authToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return ResponseResult.success(userDetails);
    }
}

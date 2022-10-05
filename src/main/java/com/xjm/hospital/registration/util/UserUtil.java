package com.xjm.hospital.registration.util;

import com.xjm.hospital.registration.security.vo.MyUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class UserUtil {


    /**
     * 获取token
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request){
        String authToken = request.getHeader("X-Token");
        if(Objects.isNull(authToken)){
            authToken = request.getHeader("token");
            if(Objects.isNull(authToken)){
                authToken = request.getParameter("token");
            }
        }
        return authToken;
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

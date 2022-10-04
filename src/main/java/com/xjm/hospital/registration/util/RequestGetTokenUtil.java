package com.xjm.hospital.registration.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class RequestGetTokenUtil {

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
}

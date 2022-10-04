package com.xjm.hospital.registration.security.handler;


import cn.hutool.json.JSONUtil;
import com.xjm.hospital.registration.resp.ResponseResult;
import com.xjm.hospital.registration.resp.ResultEnum;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description: 用户未登录时返回给前端的数据 身份验证入口点
 *
 * @author: gremlin
 * Date: 2022/6/10 17:24
 * @version: 1.0.0
 */
@Component
public class AjaxAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.getWriter().write(JSONUtil.toJsonStr(ResponseResult.failed(ResultEnum.USER_NEED_AUTHORITIES.getMessage(), ResultEnum.USER_NEED_AUTHORITIES.getCode())));
    }
}

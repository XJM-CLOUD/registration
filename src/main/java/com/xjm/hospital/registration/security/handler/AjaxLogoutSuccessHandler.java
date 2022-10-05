package com.xjm.hospital.registration.security.handler;


import cn.hutool.json.JSONUtil;
import com.xjm.hospital.registration.resp.ResponseResult;
import com.xjm.hospital.registration.resp.ErrorCodeConst;
import com.xjm.hospital.registration.util.JwtTokenUtils;
import com.xjm.hospital.registration.util.RedisUtils;
import com.xjm.hospital.registration.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description: 自定义处理注销成功的类
 *
 * @author: gremlin
 * Date: 2022/6/10 10:55
 * @version: 1.0.0
 */
@Component
@Slf4j
public class AjaxLogoutSuccessHandler implements LogoutSuccessHandler {

    @Resource
    private RedisUtils redisUtil;

    @Override
    public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException {
        // 获取头部信息的token
        String authToken = UserUtil.getToken(req);
        if (null != authToken) {
            String username = JwtTokenUtils.parseToken(authToken);
            // 清除redis里的token等其他值
            redisUtil.deleteKeys(username);
            log.info("用户登出成功！token：{}已从redis删除", authToken);
        }
        resp.setHeader("Content-type", "application/json;charset=UTF-8");
        resp.getWriter().write(JSONUtil.toJsonStr(ResponseResult.success(ErrorCodeConst.USER_LOGOUT_SUCCESS_MSG)));
    }
}

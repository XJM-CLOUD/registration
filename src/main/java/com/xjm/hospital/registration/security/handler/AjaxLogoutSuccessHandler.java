package com.xjm.hospital.registration.security.handler;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.xjm.hospital.registration.resp.ErrorCodeConst;
import com.xjm.hospital.registration.resp.ResponseResult;
import com.xjm.hospital.registration.util.MyTokenUtils;
import com.xjm.hospital.registration.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义处理注销成功的类
 *
 * @author xiangjunming
 * @date 2022/10/16
 */
@Component
@Slf4j
public class AjaxLogoutSuccessHandler implements LogoutSuccessHandler {

    @Resource
    private RedisUtils redisUtil;

    @Override
    public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException {
        // 获取头部信息的token
        String accessToken = MyTokenUtils.getCurrentRequestToken();
        if (ObjectUtil.isNotNull(accessToken)) {
            String username = MyTokenUtils.getSubject(accessToken);
            String userTokenKey = username + "_token";
            Object userToken = redisUtil.get(userTokenKey);
            // 清除redis里的token
            redisUtil.delete(username);
            log.info("用户登出成功！key:{}, value:{}", userTokenKey, userToken);
        }
        resp.setHeader("Content-type", "application/json;charset=UTF-8");
        resp.getWriter().write(JSONUtil.toJsonStr(ResponseResult.success(ErrorCodeConst.USER_LOGOUT_SUCCESS_MSG)));
    }
}

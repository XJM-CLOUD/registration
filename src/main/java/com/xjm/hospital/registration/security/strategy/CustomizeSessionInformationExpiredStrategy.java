package com.xjm.hospital.registration.security.strategy;


import cn.hutool.json.JSONUtil;
import com.xjm.hospital.registration.resp.ResponseResult;
import com.xjm.hospital.registration.resp.ResultEnum;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description: 会话信息过期策略
 *
 * @author: gremlin
 * Date: 2022/6/10 10:55
 * @version: 1.0.0
 */
@Component
public class CustomizeSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException {
        HttpServletResponse httpServletResponse = sessionInformationExpiredEvent.getResponse();
        httpServletResponse.setHeader("Content-type", "application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(JSONUtil.toJsonStr(
                ResponseResult.failed(ResultEnum.USER_NO_ACCESS.getMessage(), ResultEnum.USER_NO_ACCESS.getCode())));

    }
}
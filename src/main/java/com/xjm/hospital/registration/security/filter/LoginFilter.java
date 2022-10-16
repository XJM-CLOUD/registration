package com.xjm.hospital.registration.security.filter;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xjm.hospital.registration.resp.ErrorCodeConst;
import com.xjm.hospital.registration.resp.ErrorCodeEnum;
import com.xjm.hospital.registration.resp.ResponseResult;
import com.xjm.hospital.registration.security.service.TokenService;
import com.xjm.hospital.registration.security.vo.MyUserDetails;
import com.xjm.hospital.registration.util.AccessAddressUtils;
import com.xjm.hospital.registration.util.MyTokenUtils;
import com.xjm.hospital.registration.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * token登录过滤器
 *
 * @author xiangjunming
 * @date 2022/10/16
 */
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public LoginFilter(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    /**
     * 通过前端传来的json数据（用户名 密码）,解析成我们的java对象,最终得到用户名和密码去进行认证
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        //只允许post请求
        if ("POST".equals(req.getMethod())) {
            // 获取请求体
            String body = getBody(req);
            // 转换为json格式
            JSONObject jsonObject = JSONUtil.parseObj(body);
            String username = jsonObject.getStr("username");
            String password = jsonObject.getStr("password");
            username = username != null ? username.trim() : "";
            password = password != null ? password : "";
            UsernamePasswordAuthenticationToken authRequest =
                    new UsernamePasswordAuthenticationToken(username, password);

            // 获取ip地址
            //String ip = AccessAddressUtils.getIpAddress(req);
            // 插入用户登录成功日志
            //logService.insertLog(ip, "1", "登入", username);
            return authenticationManager.authenticate(authRequest);
        }
        return null;
    }

    /**
     * 若认证成功,根据用户名生成token返回给前端。并以用户名为key,权限列表为value的形式存入redis缓存中
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                            Authentication auth) throws IOException {
        res.setHeader("Content-type", "application/json;charset=UTF-8");
        // 获取IP地址
        String ip = AccessAddressUtils.getIpAddress();
        Map<String, Object> payloads = MapUtil.of("ip", ip);
        // 获取当前用户信息
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Map<String, Object> result = tokenService.generateToken(userDetails, payloads);
        res.getWriter().write(JSONUtil.toJsonStr(ResponseResult.success(ErrorCodeConst.USER_LOGIN_SUCCESS_MSG, result)));
    }

    /**
     * 登录失败 若认证失败，则返回错误信息给前端
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException e) throws IOException {
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        if (e instanceof UsernameNotFoundException) {
            response.getWriter().write(JSONUtil.toJsonStr(ResponseResult.failed(ErrorCodeEnum.USER_NOT_FIND)));
        } else if (e instanceof BadCredentialsException) {
            response.getWriter().write(JSONUtil.toJsonStr(ResponseResult.failed(ErrorCodeEnum.USER_LOGIN_FAILED)));
        } else if (e instanceof LockedException) {
            response.getWriter().write(JSONUtil.toJsonStr(ResponseResult.failed(ErrorCodeEnum.USER_LOCKED_FIND)));
        } else {
            response.getWriter().write(JSONUtil.toJsonStr(ResponseResult.failed(ErrorCodeEnum.USER_LOGIN_FAILED)));
        }
    }

    /**
     * 获取请求Body
     */
    public String getBody(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = request.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}

package com.xjm.hospital.registration.security.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xjm.hospital.registration.resp.ResponseResult;
import com.xjm.hospital.registration.resp.ErrorCodeConst;
import com.xjm.hospital.registration.resp.ErrorCodeEnum;
import com.xjm.hospital.registration.security.vo.MyUserDetails;
import com.xjm.hospital.registration.util.AccessAddressUtils;
import com.xjm.hospital.registration.util.JwtTokenUtils;
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
 * Description: token登录过滤器
 *
 * @author: gremlin
 * Date: 2022/6/10 10:55
 * @version: 1.0.0
 */
@Slf4j
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final RedisUtils redisUtil;
    private final AuthenticationManager authenticationManager;

    public TokenLoginFilter(AuthenticationManager authenticationManager, RedisUtils redisUtil) {
        this.authenticationManager = authenticationManager;
        this.redisUtil = redisUtil;
    }

    /**
     * 通过前端传来的json数据（用户名 密码）,解析成我们的java对象,最终得到用户名和密码去进行认证
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        System.out.println(2);
        //只允许post请求
        if ("POST".equals(req.getMethod())) {
            // 获取请求体
            String body = getBody(req);
            // 转换为json格式
            JSONObject jsonObject = JSONUtil.parseObj(body);
            String username = jsonObject.getStr("username");
            String password = jsonObject.getStr("password");
            // 获取ip地址
            String ip = AccessAddressUtils.getIpAddress(req);
            username = username != null ? username.trim() : "";
            password = password != null ? password : "";

            UsernamePasswordAuthenticationToken authRequest =
                    new UsernamePasswordAuthenticationToken(username, password);

            //插入用户登录成功日志
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
        System.out.println(4);
        Map<String, Object> map = new HashMap<>();
        // 获取IP地址
        String ip = AccessAddressUtils.getIpAddress(req);
        map.put("ip", ip);
        // 获取当前用户信息
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();

        // 通过jwt生成jwtToken
        String jwtToken = JwtTokenUtils.generateToken(userDetails.getUsername(), map);
        // 将token赋值到用户对象类
        userDetails.setToken(jwtToken);

        List<String> roles = StrUtil.split(userDetails.getRoles(), StrUtil.C_COMMA);
        map.put("roles", roles);
        //获取请求的ip地址
        redisUtil.setTokenRefresh(userDetails.getUsername(), userDetails.getToken(), ip);

        log.info("用户{}登录成功，信息已保存至redis", userDetails.getUsername());
        res.setHeader("Content-type", "application/json;charset=UTF-8");
        map.put("token", jwtToken);
        redisUtil.set(userDetails.getUsername() + "token", jwtToken);
        res.getWriter().write(JSONUtil.toJsonStr(ResponseResult.success(ErrorCodeConst.USER_LOGIN_SUCCESS_MSG, map)));
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

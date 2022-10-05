package com.xjm.hospital.registration.security.filter;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.xjm.hospital.registration.resp.ResponseResult;
import com.xjm.hospital.registration.resp.ErrorCodeEnum;
import com.xjm.hospital.registration.security.UserDetailsServiceImpl;
import com.xjm.hospital.registration.util.DateUtil;
import com.xjm.hospital.registration.util.JwtTokenUtils;
import com.xjm.hospital.registration.util.RedisUtils;
import com.xjm.hospital.registration.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: token认证过滤器
 *
 * @author: gremlin
 * Date: 2022/6/10 10:55
 * @version: 1.0.0
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private UserDetailsServiceImpl userDetailsService;
    @Resource
    private RedisUtils redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println(1);
        String authToken = UserUtil.getToken(request);
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        if (null != authToken) {
            //判断token是否有效
            if (JwtTokenUtils.isTokenExpired(authToken)) {
                response.getWriter().write(JSONUtil.toJsonStr(ResponseResult.failed(ErrorCodeEnum.LOGIN_IS_OVERDUE)));
                return;
            }
            // 通过token获取用户名
            String username = JwtTokenUtils.parseToken(authToken);
            if (StringUtils.isBlank(username)) {
                response.getWriter().write(JSONUtil.toJsonStr(ResponseResult.failed(ErrorCodeEnum.LOGIN_IS_OVERDUE)));
                return;
            }
            String ip = MapUtil.getStr(JwtTokenUtils.getClaims(authToken), "ip");
            //判断redis是否有保存
            if (redisUtil.hasKey(username)) {
                //有效时间
                String expirationTime = redisUtil.hasGet(username, "expirationTime").toString();
                if (JwtTokenUtils.isExpiration(expirationTime)) {
                    // token失效
                    //当前时间超过有效时间，用户登录失效
                    //获得redis中用户的token刷新时效
                    String tokenValidTime = (String) redisUtil.getTokenValidTimeByToken(username);
                    String currentTime = DateUtil.getTime();
                    //这个token已作废，加入黑名单
                    if (DateUtil.compareDate(currentTime, tokenValidTime) && !DateUtil.compareDate(tokenValidTime, expirationTime)) {
                        //超过有效期，不予刷新
                        log.info("{}已超过有效期，不予刷新", authToken);
                        response.getWriter().write(JSONUtil.toJsonStr(ResponseResult.failed(ErrorCodeEnum.LOGIN_IS_OVERDUE)));
                        return;
                    } else {
                        //仍在有效时间内,判断是否到达刷新时间，如果到达刷新时间
                        // （刷新时间判断方法为token时间-当前时间<=5min），刷新token否则不更新token
                        Date tokenTime = JwtTokenUtils.getTokenTime(authToken);
                        // 则刷新token，放入请求头中
                        String usernameByToken = (String) redisUtil.getUsernameByToken(username);
                        //更新username
                        username = usernameByToken;
                        //更新ip
                        ip = (String) redisUtil.getIpByToken(username);
                        //token中的时间与当前时间做对比，
                        if (DateUtil.compareDate(tokenTime, new Date())) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("ip", ip);
                            String jwtToken = JwtTokenUtils.generateToken(usernameByToken, map);
                            redisUtil.setTokenRefresh(username, jwtToken, ip);
                            log.info("redis已删除旧token：{},新token：{}已更新redis", authToken, jwtToken);
                            //更新token，为了后面
                            authToken = jwtToken;
                        }
                        redisUtil.set(username + "token", authToken, 1200);
                    }
                }
            } else {
                log.info("{}redis登录信息不存在", username);
                response.getWriter().write(JSONUtil.toJsonStr(ResponseResult.failed(ErrorCodeEnum.LOGIN_IS_OVERDUE)));
                return;
            }
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    response.reset();
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
package com.xjm.hospital.registration.security.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.xjm.hospital.registration.resp.ErrorCodeEnum;
import com.xjm.hospital.registration.resp.ResponseResult;
import com.xjm.hospital.registration.security.UserDetailsServiceImpl;
import com.xjm.hospital.registration.util.MyTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * token认证过滤器
 *
 * @author xiangjunming
 * @date 2022/10/16
 */
@Slf4j
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    private UserDetailsServiceImpl userDetailsService;

    public AuthenticationTokenFilter(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    private List<String> antMatchers(String... antPatterns) {
        return Stream.of(antPatterns).collect(Collectors.toList());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean authStatusFlag = antMatchers(
                "/swagger**/**",
                "/swagger-ui.html",
                "/swagger-resources/**",
                "/webjars/**",
                "/v3/**",
                "/token/refreshToken"
        ).stream().anyMatch(pathPattern -> antPathMatcher.match(pathPattern, requestURI));
        if (authStatusFlag) {
            // 不拦截放行
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = MyTokenUtils.getCurrentRequestToken();
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        if (StrUtil.isNotBlank(accessToken)) {
            if (!MyTokenUtils.isAccessToken(accessToken)) {
                response.getWriter().write(JSONUtil.toJsonStr(ResponseResult.failed(ErrorCodeEnum.ACCESS_TOKEN_NOT_TYPE)));
                return;
            }
            //判断accessToken是否有效
            if (!MyTokenUtils.validate(accessToken)) {
                response.getWriter().write(JSONUtil.toJsonStr(ResponseResult.failed(ErrorCodeEnum.ACCESS_TOKEN_IS_OVERDUE)));
                return;
            }
            String username = MyTokenUtils.getSubject(accessToken);

            // 设置当前用户
            if (StrUtil.isNotBlank(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    response.reset();
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            // token验证通过放行
            filterChain.doFilter(request, response);
        } else {
            // accessToken null
            response.getWriter().write(JSONUtil.toJsonStr(ResponseResult.failed(ErrorCodeEnum.ACCESS_TOKEN_IS_NULL)));
        }
    }
}
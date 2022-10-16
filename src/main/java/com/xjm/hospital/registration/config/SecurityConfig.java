package com.xjm.hospital.registration.config;

import com.xjm.hospital.registration.security.UserDetailsServiceImpl;
import com.xjm.hospital.registration.security.filter.AuthenticationTokenFilter;
import com.xjm.hospital.registration.security.filter.LoginFilter;
import com.xjm.hospital.registration.security.handler.AjaxAccessDeniedHandler;
import com.xjm.hospital.registration.security.handler.AjaxAuthenticationEntryPoint;
import com.xjm.hospital.registration.security.handler.AjaxLogoutSuccessHandler;
import com.xjm.hospital.registration.security.service.TokenService;
import com.xjm.hospital.registration.security.strategy.CustomizeSessionInformationExpiredStrategy;
import com.xjm.hospital.registration.util.RedisUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 *
 * @author xiangjunming
 * @date 2022/10/15
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    /**
     * 注销成功返回的 JSON 格式数据给前端
     */
    @Resource
    private AjaxLogoutSuccessHandler logoutSuccessHandler;
    /**
     * 无权访问 JSON 格式的数据
     */
    @Resource
    private AjaxAccessDeniedHandler ajaxAccessDeniedHandler;
    @Resource
    private AjaxAuthenticationEntryPoint authenticationEntryPoint;
    @Resource
    private CustomizeSessionInformationExpiredStrategy sessionInformationExpiredStrategy;
    @Resource
    private TokenService tokenService;
    @Resource
    private UserDetailsServiceImpl userDetailsService;
    /**
     * 注入AuthenticationConfiguration
     */
    @Resource
    private AuthenticationConfiguration auth;

    /**
     * 编写AuthenticationManager的bean
     */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return auth.getAuthenticationManager();
    }

    /**
     * 替换旧版本中的configure(HttpSecurity http)方法
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and().authorizeRequests()
                //自定义放行接口
                .antMatchers(
                        "/swagger**/**",
                        "/swagger-ui.html",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/v3/**",
                        "/token/refreshToken"
                ).permitAll()
                .anyRequest()
                .authenticated()
                .and().logout().logoutUrl("/logout")
                //登出处理
                .logoutSuccessHandler(logoutSuccessHandler)
                //添加关于自定义的认证过滤器和自定义的授权过滤器
                .and()
                .logout().permitAll()//注销行为任意访问
                //会话管理
                .and().sessionManagement()
                //同一账号同时登录最大用户数
                .maximumSessions(1)
                //会话信息过期策略会话信息过期策略(账号被挤下线)
                .expiredSessionStrategy(sessionInformationExpiredStrategy);

        //自定义权限拒绝处理类
        // 无权访问 JSON 格式的数据
        http.exceptionHandling().accessDeniedHandler(ajaxAccessDeniedHandler);
        // 登录验证
        http.addFilter(new LoginFilter(authenticationManager(), tokenService)).httpBasic();
        // JWT Filter
        http.addFilterBefore(new AuthenticationTokenFilter(userDetailsService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("123456"));
    }
}
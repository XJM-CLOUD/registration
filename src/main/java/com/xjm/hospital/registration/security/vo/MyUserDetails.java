package com.xjm.hospital.registration.security.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 自定义的实体类，实现于spring security框架的UserDetails
 *
 * @author xiangjunming
 * @date 2022/10/16
 */
public class MyUserDetails implements UserDetails {

    @Schema(name = "userid", description = "用户ID")
    private Long userid;

    @Schema(name = "username", description = "用户名")
    private String username;

    @Schema(name = "password", description = "用户密码")
    private String password;

    @Schema(name = "roles", description = "用户角色")
    private String roles;

    @Schema(name = "accessToken", description = "访问令牌")
    private String accessToken;

    @Schema(name = "refreshToken", description = "刷新令牌")
    private String refreshToken;

    public MyUserDetails(Long userid, String username, String password, String roles, String accessToken, String refreshToken) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getRoles() {
        return roles;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
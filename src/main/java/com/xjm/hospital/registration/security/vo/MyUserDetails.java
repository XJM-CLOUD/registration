package com.xjm.hospital.registration.security.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Description: 自定义的实体类，实现于spring security框架的UserDetails
 *
 * @author: gremlin
 * Date: 2022/6/10 13:06
 * @version: 1.0.0
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

    @Schema(name = "token", description = "用户token值")
    private String token;

    public MyUserDetails(Long userid, String username, String password, String roles, String token) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.token = token;
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

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
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
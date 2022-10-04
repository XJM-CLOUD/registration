package com.xjm.hospital.registration.entity;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user", schema = "hospital", catalog = "")
@Data
public class UserEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id")
    private long userId;
    @Basic
    @Column(name = "user_name")
    private String userName;
    @Basic
    @Column(name = "user_pwd")
    private String userPwd;
    @Basic
    @Column(name = "real_name")
    private String realName;
    @Basic
    @Column(name = "phone")
    private String phone;
    @Basic
    @Column(name = "roles")
    private String roles;
    @Basic
    @Column(name = "enable_status")
    private Integer enableStatus;
    @Basic
    @Column(name = "create_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;


    public static void main(String[] args) {
        UserEntity user = new UserEntity();
        user.setUserId(IdUtil.getSnowflakeNextId());
        user.setUserName("13310266227");
        user.setUserPwd("123456");
        user.setPhone("13310266227");
        user.setEnableStatus(1);
        user.setRealName("向先生");
        user.setCreateTime(LocalDateTime.now());

        System.out.println(JSONUtil.toJsonStr(user));
    }
}

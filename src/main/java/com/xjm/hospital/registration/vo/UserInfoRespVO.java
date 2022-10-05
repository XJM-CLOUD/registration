package com.xjm.hospital.registration.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoRespVO {
    private String name;
    private String avatar;
    private List<String> roles;
}

package com.xjm.hospital.registration.query;

import lombok.Data;

@Data
public class UserQuery extends PageQuery{

    private String userId;
    private String userName;
    private String phone;
}

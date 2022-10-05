package com.xjm.hospital.registration.query;

import lombok.Data;

@Data
public class PageQuery {

    private int page = 0;
    private int limit = 20;
    private String sort;

}

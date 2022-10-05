package com.xjm.hospital.registration.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRespVO<T> {
    private List<T> items;
    private Long total;
}

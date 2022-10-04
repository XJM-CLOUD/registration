package com.xjm.hospital.registration.controller;

import com.xjm.hospital.registration.resp.ResponseResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping("test1")
    public ResponseResult<String> test1() {
        return ResponseResult.success("test1");
    }
}

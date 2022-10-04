package com.xjm.hospital.registration.resp;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * Description: 结果响应json返回类
 *
 * @author: gremlin
 * Date: 2022/6/9 15:54
 * @version: 1.0.0
 */
@Slf4j
@Data
public class ResponseResult<T> implements Serializable {

    @Schema(name = "data", description = "返回数据")
    private T data;
    @Schema(name = "message", description = "返回信息")
    private String message;
    @Schema(name = "code", description = "结果code")
    private Integer code;
    @Schema(name = "isSuccess", description = "是否成功")
    private Boolean isSuccess = true;

    public ResponseResult() {
    }

    /**
     * 成功
     */
    public ResponseResult(T data, Integer code, String message) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功
     */
    public ResponseResult(T data, Integer code, String message, Boolean isSuccess) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.isSuccess = isSuccess;
    }

    /**
     * 失败
     */
    public ResponseResult(String message, Integer code) {
        this.code = code;
        this.message = message;
    }

    /**
     * 失败
     */
    public ResponseResult(String message) {
        this.message = message;
    }

    /**
     * 方便静态调用(成功)
     */
    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<T>(data, ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 方便静态调用(成功)
     */
    public static <T> ResponseResult<T> success(T data, Integer code, String message) {
        return new ResponseResult<T>(data, code, message);
    }

    /**
     * 方便静态调用(成功)
     */
    public static <T> ResponseResult<T> success(T data, Integer code, String message, Boolean isSuccess) {
        return new ResponseResult<T>(data, code, message, isSuccess);
    }

    /**
     * 方便静态调用(失败)
     */
    public static <T> ResponseResult<T> failed() {
        return new ResponseResult<T>(ResultEnum.SUCCESS.getMessage(), ResultEnum.SUCCESS.getCode());
    }

    /**
     * 方便静态调用(失败)
     */
    public static <T> ResponseResult<T> failed(String message, Integer code) {
        return new ResponseResult<T>(message, code);
    }

    /**
     * 方便静态调用(失败)
     */
    public static <T> ResponseResult<T> failed(String message) {
        return new ResponseResult<T>(message);
    }
}
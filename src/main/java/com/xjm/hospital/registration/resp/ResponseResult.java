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

    @Schema(name = "code", description = "结果code")
    private Integer code;
    @Schema(name = "message", description = "返回信息")
    private String message;
    @Schema(name = "data", description = "返回数据")
    private T data;
    @Schema(name = "isSuccess", description = "是否成功")
    private Boolean isSuccess = true;

    public ResponseResult(Integer code, String message, T data, Boolean isSuccess) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.isSuccess = isSuccess;
    }

    /**
     * 方便静态调用(成功)
     */
    public static <T> ResponseResult<T> success() {
        return success(null);
    }

    /**
     * 方便静态调用(成功)
     */
    public static <T> ResponseResult<T> success(T data) {
        return success(ErrorCodeEnum.SUCCESS.getMessage(), data);
    }
    /**
     * 方便静态调用(成功)
     */
    public static <T> ResponseResult<T> success(String message) {
        return success(message, null);
    }
    /**
     * 方便静态调用(成功)
     */
    public static <T> ResponseResult<T> success(String message, T data) {
        return new ResponseResult<T>(ErrorCodeEnum.SUCCESS.getCode(), message, data, true);
    }

    /**
     * 方便静态调用(失败)
     */
    public static <T> ResponseResult<T> failed() {
        return failed(ErrorCodeEnum.FAILURE);
    }

    /**
     * 方便静态调用(失败)
     */
    public static <T> ResponseResult<T> failed(ErrorCodeEnum errorCodeEnum) {
        return failed(errorCodeEnum.getCode(), errorCodeEnum.getMessage());
    }

    /**
     * 方便静态调用(失败)
     */
    public static <T> ResponseResult<T> failed(String message) {
        return failed(ErrorCodeEnum.FAILURE.getCode(), message);
    }

    /**
     * 方便静态调用(失败)
     */
    public static <T> ResponseResult<T> failed(Integer code, String message) {
        return new ResponseResult<T>(code, message, null, false);
    }


}
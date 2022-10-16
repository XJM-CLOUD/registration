package com.xjm.hospital.registration.exception;

import com.xjm.hospital.registration.resp.ErrorCodeEnum;

/**
 * token自定义异常类
 *
 * @author xiangjunming
 * @date 2022/10/16
 */
public class ErrorCodeException extends RuntimeException{
    private ErrorCodeEnum errorCode;

    public ErrorCodeException(ErrorCodeEnum errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCodeEnum getErrorCode() {
        return errorCode;
    }
}

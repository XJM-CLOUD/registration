package com.xjm.hospital.registration.exception;

import com.xjm.hospital.registration.resp.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * @author xiangjunming
 * @date 2022/10/16
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ErrorCodeException.class)
    public ResponseEntity<ResponseResult> handlerErrorCodeException(ErrorCodeException e){
        log.error("error:", e);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseResult.failed(e.getErrorCode()));
    }
}

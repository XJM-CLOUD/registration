package com.xjm.hospital.registration.resp;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Description: 结果code枚举类
 * @author: gremlin
 * Date: 2022/6/9 16:13
 * @version: 1.0.0
 */
@Getter
public enum ErrorCodeEnum {

    /**
     * 请求成功
     */
    SUCCESS(200,"请求成功"),
    /**
     * 请求失败
     */
    FAILURE(99,"请求失败"),
    /**
     * 用户未登录
     */
    USER_NEED_AUTHORITIES(201,"用户未登录"),
    /**
     * 用户账号或密码错误
     */
    USER_LOGIN_FAILED(202,"用户账号或密码错误"),
    /**
     * 用户不存在
     */
    USER_NOT_FIND(203,"该用户不存在或被禁用请联系管理员"),
    /**
     * 用户已被锁定
     */
    USER_LOCKED_FIND(204,"用户已被锁定"),
    /**
     * 用户无权访问
     */
    USER_NO_ACCESS(205,"用户无权访问"),
    /**
     * access_token不能为空
     */
    ACCESS_TOKEN_IS_NULL(206,"access_token不能为空"),
    /**
     * refresh_token不能为空
     */
    REFRESH_TOKEN_IS_NULL(207,"refresh_token不能为空"),
    /**
     * access_token类型错误
     */
    ACCESS_TOKEN_NOT_TYPE(208,"access_token类型错误"),
    /**
     * accessToken已过期, 请重新获取
     */
    ACCESS_TOKEN_IS_OVERDUE(209,"access_token已过期, 请重新获取"),
    /**
     * refresh已过期，请重新登录
     */
    REFRESH_TOKEN_IS_OVERDUE(210,"refresh_token已过期，请重新登录"),
    /**
     * 手机号码不正确
     */
    ERROR_PHONE(50001,"手机号码不正确"),
    /**
     * 邮箱地址不正确
     */
    ERROR_EMAIL(50002,"邮箱地址不正确"),
    /**
     * 验证码错误
     */
    ERROR_CODE(50003,"验证码错误"),

    ;
    @Schema(name = "code",description = "返回code")
    private final Integer code;
    @Schema(name = "message",description = "返回信息")
    private final String message;

    ErrorCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}

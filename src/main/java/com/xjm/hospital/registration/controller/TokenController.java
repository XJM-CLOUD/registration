package com.xjm.hospital.registration.controller;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xjm.hospital.registration.query.UserQuery;
import com.xjm.hospital.registration.resp.ErrorCodeEnum;
import com.xjm.hospital.registration.resp.ResponseResult;
import com.xjm.hospital.registration.security.service.TokenService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

import static com.xjm.hospital.registration.resp.ErrorCodeConst.REFRESH_TOKEN_SUCCESS_MSG;

/**
 * 
 * @author xiangjunming
 * @date 2022/10/17
 */
@RestController
@RequestMapping("token")
public class TokenController {

    @Resource
    private TokenService tokenService;

    @PostMapping("refreshToken")
    public ResponseResult<Map<String, Object>> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        if(StrUtil.isBlank(refreshToken)){
            return ResponseResult.failed(ErrorCodeEnum.REFRESH_TOKEN_IS_NULL);
        }
        Map<String, Object> result = tokenService.refreshToken(refreshToken);
        return ResponseResult.success(REFRESH_TOKEN_SUCCESS_MSG, result);
    }

}

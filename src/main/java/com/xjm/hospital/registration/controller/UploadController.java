package com.xjm.hospital.registration.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.xjm.hospital.registration.resp.ResponseResult;
import com.xjm.hospital.registration.util.QiniuUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.xjm.hospital.registration.resp.ErrorCodeConst.UPLOAD_SUCCESS_MSG;

@RestController
public class UploadController {

    /**
     * 上传文件到七牛云
     * @param request
     * @param uploadFile
     * @return
     */
    @RequestMapping("/upload")
    public ResponseResult<String> upload(HttpServletRequest request, MultipartFile uploadFile) {
        try {
            String originalFilename = uploadFile.getOriginalFilename();
            // 雪花算法ID
            long snowflakeNextId = IdUtil.getSnowflakeNextId();
            // 原文件后缀
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 上传文件名
            String fileName = snowflakeNextId + StrUtil.C_DOT + suffix;
            // 上传七牛云返回key文件名
            String key = QiniuUtils.upload(uploadFile.getBytes(), fileName);
            return ResponseResult.success(UPLOAD_SUCCESS_MSG, key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

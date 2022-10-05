package com.xjm.hospital.registration.util;

import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QiniuUtils {

    /**
     * 上传文件七牛云
     * @param uploadBytes
     * @return
     */
    public static String upload(byte[] uploadBytes, String fileName) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "EQA1xTO3bQui3gWTeNMkBxJ0gug_v_1Geg78_ckr";
        String secretKey = "5oaXQdW_xbkiyNRy6jy2bs7T-xd-t_Ybp758BOKx";
        String bucket = "xjmstore";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = "registration/" + fileName;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(uploadBytes, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            log.info("上传文件七牛云成功!, resp:{}", JSONUtil.toJsonStr(putRet));
            return putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            log.error(r.toString());
            try {
                log.error(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }

        return null;
    }

}

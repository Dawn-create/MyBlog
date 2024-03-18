package com.itdawn.service.impl;

import com.google.gson.Gson;
import com.itdawn.domain.ResponseResult;
import com.itdawn.enums.AppHttpCodeEnum;
import com.itdawn.exception.SystemException;
import com.itdawn.service.OssUploadService;
import com.itdawn.utils.PathUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;

@Service
@Data//为成员变量生成get和set方法
@ConfigurationProperties(prefix = "oss")
public class OssUploadServiceImpl implements OssUploadService {
    @Override
    public ResponseResult uploadImg(MultipartFile img) {
        //获取原始文件名
        String originalFilename = img.getOriginalFilename();
        // 获取文件大小
        long fileSize = img.getSize();
        // 判断文件大小是否超过2MB（2MB=2*1024*1024 bytes）
        if (fileSize > 2 * 1024 * 1024) {
            // 抛出文件大小超过限制的异常
            throw new SystemException(AppHttpCodeEnum.FILE_SIZE_ERROR);
        }
        //对原始文件名进行判断大小。只能上传png或jpg文件
        if(!originalFilename.endsWith(".png") && !originalFilename.endsWith(".jpg")) {
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        //如果满足判断条件，则上传文件到七牛云OSS，并得到一个图片外链访问地址。PathUtil是在dawn-framework工程写的工具类
        String filePath = PathUtils.generateFilePath(originalFilename);
        //下面用于调用的uploadOss方法返回的必须是String类型
        String url = uploadOss(img, filePath);
        return ResponseResult.okResult(url);
    }
    //----------------------------------上传文件到七牛云----------------------------------------

    private String accessKey;
    private String secretKey;
    private String bucket;

    private String uploadOss(MultipartFile imgFile, String filePath) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        UploadManager uploadManager = new UploadManager(cfg);

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filePath;

        try {
            InputStream inputStream = imgFile.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(inputStream, key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println("上传成功! 生成的key是: "+putRet.key);
                System.out.println("上传成功! 生成的hash是: "+putRet.hash);
                return"http://sadumlu55.hd-bkt.clouddn.com"+key;

            } catch (QiniuException ex) {
                ex.printStackTrace();
                if (ex.response != null) {
                    System.err.println(ex.response);

                    try {
                        String body = ex.response.toString();
                        System.err.println(body);
                    } catch (Exception ignored) {
                    }
                }
            }
        } catch (Exception ex) {
            //ignore
        }
        return "上传失败";

    }
}

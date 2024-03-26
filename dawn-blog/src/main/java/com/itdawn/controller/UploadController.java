package com.itdawn.controller;

import com.itdawn.domain.ResponseResult;
import com.itdawn.service.OssUploadService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(tags = "上传文件的相关接口文档", description = "我是描述信息")

public class UploadController {

    @Autowired
    private OssUploadService ossUploadService;

    @PostMapping("/upload")
    //MultipartFile是spring提供的接口
    public ResponseResult uploadImg(MultipartFile img){
        //图片上传到七牛云
        return ossUploadService.uploadImg(img);
    }
}
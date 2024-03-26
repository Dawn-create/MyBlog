package com.itdawn.controller;

import com.itdawn.domain.ResponseResult;
import com.itdawn.service.LinkService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/link")
@Api(tags = "友链的相关接口文档", description = "我是描述信息")

public class LinkController {

    @Autowired
    private LinkService linkService;
    @GetMapping("/getAllLink")
    //ResponseResult是dawn-framework工程写的实体类
    public ResponseResult getAllLink(){
        return linkService.getAllLink();
    }
}

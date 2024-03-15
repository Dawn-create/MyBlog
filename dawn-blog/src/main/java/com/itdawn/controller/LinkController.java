package com.itdawn.controller;

import com.itdawn.domain.ResponseResult;
import com.itdawn.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/link")
public class LinkController {

    @Autowired
    private LinkService linkService;
    @GetMapping("/getAllLink")
    //ResponseResult是dawn-framework工程写的实体类
    public ResponseResult getAllLink(){
        return linkService.getAllLink();
    }
}

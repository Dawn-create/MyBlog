package com.itdawn.controller;

import com.itdawn.annotation.SystemLog;
import com.itdawn.domain.ResponseResult;
import com.itdawn.domain.entity.User;
import com.itdawn.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Api(tags = "用户的相关接口文档", description = "我是描述信息")

public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/userInfo")
    @SystemLog(businessName = "查询个人信息")
    public ResponseResult userInfo() {
        return userService.userInfo();
    }


    @PutMapping("/userInfo")
    @SystemLog(businessName = "更新用户信息")
    public ResponseResult updateUserInfo(@RequestBody User user){
        return userService.updateUserInfo(user);
    }

    @PostMapping("/register")
    @SystemLog(businessName = "用户注册")
    public ResponseResult register(@RequestBody User user){
        //注册功能
        return userService.register(user);
    }
}

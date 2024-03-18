package com.itdawn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itdawn.domain.ResponseResult;
import com.itdawn.domain.entity.User;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;


@Service
public interface UserService extends IService<User> {
    //查询个人信息
    ResponseResult userInfo();

    //更新个人信息
    ResponseResult updateUserInfo(User user);


    ResponseResult register(User user);
}
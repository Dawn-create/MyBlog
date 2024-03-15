package com.itdawn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itdawn.domain.ResponseResult;
import com.itdawn.domain.entity.User;
import org.springframework.stereotype.Service;



public interface UserService extends IService<User> {
    ResponseResult userInfo();
}
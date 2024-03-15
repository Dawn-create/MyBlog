package com.itdawn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itdawn.domain.ResponseResult;
import com.itdawn.domain.entity.User;

/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2024-03-12 15:59:05
 */
public interface BlogLoginService {  //注意这里不需要再继承IService了

    //登陆
    ResponseResult login(User user);

    //退出登陆
    ResponseResult logout();
}
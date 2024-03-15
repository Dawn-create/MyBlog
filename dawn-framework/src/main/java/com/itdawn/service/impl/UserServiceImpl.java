package com.itdawn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itdawn.domain.ResponseResult;
import com.itdawn.domain.entity.User;
import com.itdawn.domain.vo.UserInfoVo;
import com.itdawn.mapper.UserMapper;
import com.itdawn.service.UserService;
import com.itdawn.utils.BeanCopyUtils;
import com.itdawn.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserService userService;

    @Override
    public ResponseResult userInfo() {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User user = getById(userId);
        //封装成UserInfoVo
        UserInfoVo userInfoVos= BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVos);
    }
}
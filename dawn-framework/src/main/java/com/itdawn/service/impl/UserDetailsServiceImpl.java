package com.itdawn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itdawn.domain.entity.LoginUser;
import com.itdawn.mapper.UserMapper;
import com.itdawn.domain.entity.User;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
@Service
//当dawn-blog当BlogLoginServiceImpl类封装好登录的用户名和密码之后，就会传到当前这个实现类
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    //在这之前已经拿到了登录的用户名和密码。UserDetails是SpringSecurity官方提供的接口
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {        //根据拿到的用户名，并结合查询条件(数据库是否有这个用户名)，去查询用户信息
        LambdaQueryWrapper<User> userWrapper=new LambdaQueryWrapper<>();
        userWrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(userWrapper);
        //判断是否查询到用户，如果没查到就抛出异常
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户不存在"); //后期会对异常进行统一处理
        }
        //TODO 查询权限信息，并封装
        //返回查询到的用户信息。注意下面那行直接返回user会报错
        //需要在dawn-framework工程的domain目录新建LoginUser类，在LoginUser类实现UserDetails接口，最后返回LoginUser对象
        return new LoginUser(user);
    }
}


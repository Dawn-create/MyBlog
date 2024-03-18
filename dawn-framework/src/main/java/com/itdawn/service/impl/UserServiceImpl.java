package com.itdawn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itdawn.domain.ResponseResult;
import com.itdawn.domain.entity.User;
import com.itdawn.domain.vo.UserInfoVo;
import com.itdawn.enums.AppHttpCodeEnum;
import com.itdawn.exception.SystemException;
import com.itdawn.mapper.UserMapper;
import com.itdawn.service.UserService;
import com.itdawn.utils.BeanCopyUtils;
import com.itdawn.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    //查询个人信息
    @Override
    public ResponseResult userInfo() {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User user = getById(userId);
        //封装成UserInfoVo
        UserInfoVo userInfoVos = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVos);
    }

    //更新个人信息
    @Override
    public ResponseResult updateUserInfo(User user) {
        //updateById是mybatisplus提供的方法
        updateById(user);
        return ResponseResult.okResult();
    }

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public ResponseResult register(User user) {
        //对前端传过来的用户名进行非空判断
        if (!StringUtils.hasText(user.getUserName())) {
            //SystemException是我们写的异常类、AppHttpCodeEnum是我们写的枚举类
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        //密码
        if (!StringUtils.hasText(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PWD_NOT_NULL);
        }
        //邮箱
        if (!StringUtils.hasText(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        //昵称
        if (!StringUtils.hasText(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //判断用户是否在数据库已经存在
        if (userNameExist(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);

        }

        if (nickNameExist(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);

        }

        if (emailExist(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);

        }

        //经过上面的判断，可以确保用户传给我们的用户名和昵称是数据库不存在的，且相关字段都不为空。就可以存入数据库
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库,save方法是mybatisplus提供的方法
        save(user);
        //封装响应返回
        return ResponseResult.okResult();
    }

    //判断数据库中是否存在
    private boolean userNameExist(String userName) {
        //要知道是否存在，首先就是根据条件去数据库查
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        //拿用户写的用户名跟数据库里面的用户名进行比较
        queryWrapper.eq(User::getUserName, userName);
        //如果大于0就说明从数据库查出来了，也就说明是已经存在数据库的
        boolean result = count(queryWrapper) > 0;
        //为true就说明已存在
        return result;
    }

    //'判断用户传给我们的昵称是否在数据库已经存在' 的方法
    private boolean nickNameExist(String nickName) {
        //要知道是否存在，首先就是根据条件去数据库查
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        //拿用户写的昵称跟数据库里面的昵称进行比较
        queryWrapper.eq(User::getNickName, nickName);
        //如果大于0就说明从数据库查出来了，也就说明是已经存在数据库的
        boolean result = count(queryWrapper) > 0;
        //为true就说明已存在
        return result;
    }

    //'判断用户传给我们的邮箱是否在数据库已经存在' 的方法
    private boolean emailExist(String email) {
        //要知道是否存在，首先就是根据条件去数据库查
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        //拿用户写的昵称跟数据库里面的昵称进行比较
        queryWrapper.eq(User::getEmail, email);
        //如果大于0就说明从数据库查出来了，也就说明是已经存在数据库的
        boolean result = count(queryWrapper) > 0;
        //为true就说明已存在
        return result;
    }


}
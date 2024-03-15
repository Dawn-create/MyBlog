package com.itdawn.service.impl;

import com.itdawn.domain.ResponseResult;
import com.itdawn.domain.entity.LoginUser;
import com.itdawn.domain.entity.User;
import com.itdawn.domain.vo.BlogUserLoginVo;
import com.itdawn.domain.vo.UserInfoVo;
import com.itdawn.service.BlogLoginService;
import com.itdawn.utils.BeanCopyUtils;
import com.itdawn.utils.JwtUtil;
import com.itdawn.utils.RedisCache;
import org.apache.catalina.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2024-03-12 15:59:05
 */

@Service
//认证，判断用户登录是否成功
public class BlogLoginServiceImpl implements BlogLoginService {

    @Autowired
    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisCache redisCache;

    public BlogLoginServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseResult login(User user) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
        if (userDetails == null) {
            throw new RuntimeException("用户名不存在");
        }

        // 使用passwordEncoder验证密码
        String storedPassword = userDetails.getPassword();

        // 检查提供的密码是否与UserDetails中的密码匹配
        String presentedPassword = user.getPassword();
        if (!passwordEncoder.matches(presentedPassword, storedPassword)) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 获取LoginUser对象，假设UserDetails是LoginUser类型或者可以转换为LoginUser
        LoginUser loginUser = (LoginUser) userDetails;

        // 获取userid
        String userId = loginUser.getUser().getId().toString();

        // 把这个userid通过我们写的JwtUtil工具类转成密文，这个密文就是token值
        String jwt = JwtUtil.createJWT(userId);

        // 把loginUser对象存入Redis，键为 "bloglogin:" + userId
        redisCache.setCacheObject("bloglogin:" + userId, loginUser);

        // 把User转化为UserInfoVo，再放入vo对象的第二个参数
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo vo = new BlogUserLoginVo(jwt, userInfoVo);

        // 封装响应返回
        return ResponseResult.okResult(vo);

    }

    //-----------------------------------退出登录------------------------------------------
    @Override
    public ResponseResult logout(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser=(LoginUser) authentication.getPrincipal();
        Long userid=loginUser.getUser().getId();

        //在redis根据key来删除用户的value值，注意之前我们在存key的时候，key是加了'bloglogin:'前缀
        redisCache.deleteObject("bloglogin"+userid);
        return ResponseResult.okResult();



    }

}
package com.itdawn.filter;

import com.alibaba.fastjson.JSON;
import com.itdawn.domain.ResponseResult;
import com.itdawn.domain.entity.LoginUser;
import com.itdawn.enums.AppHttpCodeEnum;
import com.itdawn.utils.JwtUtil;
import com.itdawn.utils.RedisCache;
import com.itdawn.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
//博客前台的登录认证过滤器。OncePerRequestFilter是springsecurity提供的类
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    //RedisCache是我们在huanf-framework工程写的工具类，用于操作redis
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //获取请求头中的token值
        String token = request.getHeader("token");
        //判断上面那行有没有拿到token值
        if(!StringUtils.hasText(token)){
            //说明该接口不需要登录，直接放行，不拦截
            filterChain.doFilter(request,response);
            return;
        }
        //JwtUtil是我们在dawn-framework工程写的工具类。解析获取的token，把原来的密文解析为原文
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
       } catch (Exception e) {
            // Token 过期或被篡改处理
            e.printStackTrace();
            if (e instanceof io.jsonwebtoken.ExpiredJwtException) {
                // Token 过期
                ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.EXPIRED_TOKEN);
                WebUtils.renderString(response, JSON.toJSONString(result));
            } else {
                // Token 被篡改
                System.out.println("Token 被篡改：" + token);
                ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.TAMPERED_TOKEN);
                WebUtils.renderString(response, JSON.toJSONString(result));
            }
            return;
        }
        String userid = claims.getSubject();

        //在redis中，通过key来获取value，注意key我们是加过前缀的，取的时候也要加上前缀
        LoginUser loginUser = redisCache.getCacheObject("login:" + userid);
        //如果在redis获取不到值，说明登录是过期了，需要重新登录
        if(Objects.isNull(loginUser)){
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }

        //把从redis获取到的value，存入到SecurityContextHolder(Security官方提供的类)
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request,response);

    }
}
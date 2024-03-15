package com.itdawn.handler.security;

import com.alibaba.fastjson.JSON;
import com.itdawn.domain.ResponseResult;
import com.itdawn.enums.AppHttpCodeEnum;
import com.itdawn.utils.WebUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
//自定义认证失败的处理器。ResponseResult、AppHttpCodeEnum是我们在之前在dawn-framework工程写的类
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException {
        //输出异常信息
        authenticationException.printStackTrace();
        ResponseResult result = null;
        //InsufficientAuthenticationException 权限不足或需要登陆
        //BadCredentialsException  用户名或密码错误
        if(authenticationException instanceof BadCredentialsException) {
            //第一个参数返回的是响应码，AppHttpCodeEnum是我们写的实体类。第二个参数是返回具体的信息
            result = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(), authenticationException.getMessage());
        }else if(authenticationException instanceof InsufficientAuthenticationException){
            result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }else {
            result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"认证或授权失败");
        }
        //响应给前端
        WebUtils.renderString(response, JSON.toJSONString(result));
    }
}

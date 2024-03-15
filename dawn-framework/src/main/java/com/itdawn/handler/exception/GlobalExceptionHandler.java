package com.itdawn.handler.exception;


import com.itdawn.domain.ResponseResult;
import com.itdawn.enums.AppHttpCodeEnum;
import com.itdawn.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

//@RestController可以替代@ControllerAdvice+@ResponseBody
@RestController

//使用Lombok提供的Slf4j注解，实现日志功能
@Slf4j
public class GlobalExceptionHandler {
    //用户登录的异常交给这里处理
    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e) {
        //打印异常信息，方便我们追溯问题的原因。{}是占位符，具体值由e决定
        log.error("出现异常！{}", e);
        return ResponseResult.errorResult(e.getCode(), e.getMsg());
    }

    //其它异常交给这里处理
    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e) {
        log.error("出现异常！{}", e);
        //从异常对象中获取提示信息封装，然后返回。ResponseResult、AppHttpCodeEnum是我们写的类
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());

    }
}

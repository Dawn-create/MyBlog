package com.itdawn.aspect;

import com.alibaba.fastjson.JSON;
import com.itdawn.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect //告诉spring容器LogAspect是切面类
@Slf4j //用于在控制台打印日志信息
public class LogAspect {

    //确定哪个切点，以后哪个类想成为切点，就在哪个类添加上面那行的注解。例如下面这个pt()就是切点
    @Pointcut("@annotation(com.itdawn.annotation.SystemLog)")
    public void pt() {
    }

    //定义通知的方法(这里用的是环绕通知)，通知的方法也就是增强的具体代码。@Around注解表示该通知的方法会用在哪个切点
    @Around("pt()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {     //ProceedingJoinPoint可以拿到被增强方法的信息
        //proceed相当于目标方法的调用，ret为目标方法执行后的返回值
        Object ret;
        try {
            handleBefore(joinPoint);
            ret = joinPoint.proceed(); //这是目标方法执行完成，上一行是目标方法未执行，下一行是目标方法已经执行
            handleAfter(ret);
        } finally {
            log.info("=======================End=======================" + System.lineSeparator());
        }
        return ret;
    }

    private void handleBefore(ProceedingJoinPoint joinPoint) {
        //ServletRequestAttributes是RequestAttributes是spring接口的实现类
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //下面就可以拿到请求的报文了，其中有我们需要的url、请求方式、ip。这里拿到的request会在下面的log中大量使用
        HttpServletRequest request = requestAttributes.getRequest();

        //获取被增强方法的注解对象，例如获取UserController类的updateUserInfo方法上一行的@Systemlog注解
        SystemLog systemLog = getSystemLog(joinPoint);

        log.info("======================Start======================");
        // 打印请求 URL
        log.info("请求URL   : {}", request.getRequestURL());
        // 打印描述信息
        log.info("接口描述信息    : {}", systemLog.businessName());
        // 打印 Http method
        log.info("请求方式   : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("请求类名   : {}.{}", joinPoint.getSignature().getDeclaringType());
        // 打印请求的 IP
        log.info("访问IP    : {}", request.getRemoteHost());
        // 打印请求入参。JSON.toJSONString十FastJson提供的工具方法，能把数组转成JSON
        log.info("传入参数   : {}", JSON.toJSONString(joinPoint.getArgs()));
    }

    private void handleAfter(Object ret) {
        // 打印出参。JSON.toJSONString十FastJson提供的工具方法，能把数组转成JSON
        log.info("返回参数   : {}", JSON.toJSONString(ret));
    }

    private SystemLog getSystemLog(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        SystemLog systemLog = methodSignature.getMethod().getAnnotation(SystemLog.class);
        return systemLog;
    }


}

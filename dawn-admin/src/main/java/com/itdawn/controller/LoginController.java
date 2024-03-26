package com.itdawn.controller;

import com.itdawn.domain.ResponseResult;
import com.itdawn.domain.entity.LoginUser;
import com.itdawn.domain.entity.Menu;
import com.itdawn.domain.entity.User;
import com.itdawn.domain.vo.AdminUserInfoVo;
import com.itdawn.domain.vo.RoutersVo;
import com.itdawn.domain.vo.UserInfoVo;
import com.itdawn.enums.AppHttpCodeEnum;
import com.itdawn.exception.SystemException;
import com.itdawn.service.LoginService;
import com.itdawn.service.MenuService;
import com.itdawn.service.RoleService;
import com.itdawn.utils.BeanCopyUtils;
import com.itdawn.utils.RedisCache;
import com.itdawn.utils.SecurityUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "后台系统登陆相关接口")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user) {
        if (!StringUtils.hasText(user.getUserName())) {
            //提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    //-----------------------------查询(超级管理员|非超级管理员)的权限和角色信息------------------------------------------
    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo() {
        //获取当前登录的用户。
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据用户id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        //根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
        //获取用户信息
        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);

        //封装响应返回
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms, roleKeyList, userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }
    //-------------------------------------查询路由信息(权限菜单)--------------------------------------

    @GetMapping("/getRouters")
    public ResponseResult<RoutersVo> getRouters() {
        //获取用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id来查询menu(权限菜单)。要求查询结果是tree的形式，也就是子父菜单树
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        RoutersVo routersVo = new RoutersVo(menus);
        return ResponseResult.okResult(routersVo);
    }

    //-----------------------------退出登录的接口(我们写在service比较好---------------------------------
    @Autowired
    private RedisCache redisCache;
    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }


}

package com.itdawn.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itdawn.domain.entity.Menu;

import java.util.List;

/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2024-03-24 18:30:04
 */
public interface MenuService extends IService<Menu> {
    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);
    //增加查询用户的路由信息(权限菜单)的接口

}

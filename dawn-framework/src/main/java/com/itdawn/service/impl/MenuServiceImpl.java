package com.itdawn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itdawn.constants.SystemCanstants;
import com.itdawn.domain.entity.Menu;
import com.itdawn.mapper.MenuMapper;
import com.itdawn.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2024-03-24 18:30:04
 */
@Service("menuService")

public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    //----------------------------------权限控制-------------------------------------

    @Override
    public List<String> selectPermsByUserId(Long id) {
        if (id == 1L) {
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            //查询条件是permissions中需要有所有菜单类型为C或者F的权限。SystemCanstants是我们在dawn-framework工程写的类
            queryWrapper.in(Menu::getMenuType, SystemCanstants.TYPE_MENU, SystemCanstants.TYPE_BUTTON);
            queryWrapper.eq(Menu::getStatus, SystemCanstants.STATUS_NORMAL);
            List<Menu> menus = list(queryWrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        //如果不是管理员就返回对应用户所具有的权限
        List<String> otherPerm = getBaseMapper().selectPermsByOtherUserId(id);
        return otherPerm;
    }

    //----------------------------------查询用户的路由信息(权限菜单)-------------------------------------

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper=getBaseMapper();
        List<Menu> menus=null;
        //判断是否是超级管理员，用户id为id代表超级管理员，如果是就返回所有符合要求的权限菜单
        if(userId.equals(1L)){
            menus=menuMapper.selectAllRouterMenu();
        }else{
            menus=menuMapper.selectOtherRouterMenuTreeByUserId(userId);
        }
        //构建成tree，也就是子父菜单树，有层级关系
        //思路:先找出第一层的菜单，然后再找子菜单(也就是第二层)，把子菜单的结果赋值给Menu类的children字段
        List<Menu> menuTree=buildMenuTree(menus, 0L);
        return menuTree;
    }

    //用于把List集合里面的数据构建成tree，也就是子父菜单树，有层级关系
    private List<Menu> buildMenuTree(List<Menu> menus, long parentId) {
        List<Menu> menuTree=menus.stream()
                //过滤找出父菜单树，也就是第一层
                .filter(menu -> menu.getParentId().equals(parentId))
                //getChildren是我们在下面写的方法，用于获取子菜单的List集合
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList=menus.stream()
                //通过过滤得到子菜单
                .filter(m->m.getParentId().equals(menu.getId()))
                //如果有三层次旦，也就是子菜单的子菜单，就用下面的递归来处理
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
}


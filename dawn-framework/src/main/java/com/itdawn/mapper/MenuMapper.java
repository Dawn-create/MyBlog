package com.itdawn.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itdawn.domain.entity.Menu;

import java.util.List;

/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2024-03-24 18:30:04
 */
public interface MenuMapper extends BaseMapper<Menu> {
    List<String> selectPermsByOtherUserId(Long id);

    List<Menu> selectAllRouterMenu();

    List<Menu> selectOtherRouterMenuTreeByUserId(Long userId);
}

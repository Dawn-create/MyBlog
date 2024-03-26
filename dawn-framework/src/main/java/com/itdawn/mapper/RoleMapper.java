package com.itdawn.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itdawn.domain.entity.Role;

import java.util.List;

/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2024-03-24 18:26:09
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByOtherUserId(Long id);
}

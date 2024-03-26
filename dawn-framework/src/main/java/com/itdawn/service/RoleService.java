package com.itdawn.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itdawn.domain.entity.Role;

import java.util.List;

/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2024-03-24 18:26:09
 */
public interface RoleService extends IService<Role> {
    List<String> selectRoleKeyByUserId(Long id);
}

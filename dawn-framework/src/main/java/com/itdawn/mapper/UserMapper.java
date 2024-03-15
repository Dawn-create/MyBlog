package com.itdawn.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itdawn.domain.entity.User;
import org.springframework.stereotype.Service;

/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2024-03-12 15:31:51
 */
@Service
public interface UserMapper extends BaseMapper<User> {
}

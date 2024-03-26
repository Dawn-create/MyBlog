package com.itdawn.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itdawn.domain.entity.Tag;
import org.apache.ibatis.annotations.Param;

/**
 * 标签(BlogTag)表数据库访问层
 *
 * @author makejava
 * @since 2024-03-24 16:13:23
 */
public interface TagMapper extends BaseMapper<Tag> {
    //删除标签，注意是逻辑删除，所以不能使用mybatisplus提供的，我们要自己使用mybatis写SQL语句
    int myUpdateById(@Param("id") Long id, @Param("flag") int flag);
}

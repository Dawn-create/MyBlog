package com.itdawn.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itdawn.domain.ResponseResult;
import com.itdawn.domain.entity.Tag;
import com.itdawn.domain.vo.PageVo;
import com.itdawn.domain.vo.TagVo;
import com.itdawn.dto.TagListDto;

import java.util.List;

/**
 * 标签(BlogTag)表服务接口
 *
 * @author makejava
 * @since 2024-03-24 16:13:25
 */
public interface TagService extends IService<Tag> {

    //查询标签列表
    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    //新增标签


    List<TagVo> listAllTag();
}

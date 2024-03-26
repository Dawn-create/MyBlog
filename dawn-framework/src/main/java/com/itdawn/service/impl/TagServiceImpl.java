package com.itdawn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itdawn.domain.ResponseResult;
import com.itdawn.domain.entity.Tag;
import com.itdawn.domain.vo.PageVo;
import com.itdawn.domain.vo.TagVo;
import com.itdawn.dto.TagListDto;
import com.itdawn.mapper.TagMapper;
import com.itdawn.service.TagService;
import com.itdawn.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 标签(BlogTag)表服务实现类
 *
 * @author makejava
 * @since 2024-03-24 16:13:25
 */
@Service

public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Override
    //查询标签列表
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {

        //分页查询的条件。模糊+分页
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        //第二、三个参数是互相比较。第一个参数是判空，当用户没有查询条件时，就不去比较后面两个参数
        queryWrapper.like(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
        //第二、三个参数是互相比较。第一个参数是判空，当用户没有查询条件时，就不去比较后面两个参数
        queryWrapper.like(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());

        //分页查询。Page是mybatisplus提供的类
        Page<Tag> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,queryWrapper);

        //封装数据返回。
        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public List<TagVo> listAllTag() {
        LambdaQueryWrapper<Tag> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId, Tag::getName);
        List<Tag> list=list(queryWrapper);
        List<TagVo> tagVos= BeanCopyUtils.copyBeanList(list, TagVo.class);
        return tagVos;
    }
}


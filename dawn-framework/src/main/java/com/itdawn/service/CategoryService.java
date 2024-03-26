package com.itdawn.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itdawn.domain.ResponseResult;
import com.itdawn.domain.entity.Category;
import com.itdawn.domain.vo.CategoryVo;

import java.util.List;

/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2024-03-12 06:25:51
 */
public interface CategoryService extends IService<Category> {
    //查询文章分类的接口
    ResponseResult getCategoryList();

    List<CategoryVo> listAllCategory();
}

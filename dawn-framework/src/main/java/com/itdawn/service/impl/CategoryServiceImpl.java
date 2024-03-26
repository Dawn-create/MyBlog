package com.itdawn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itdawn.constants.SystemCanstants;
import com.itdawn.domain.ResponseResult;
import com.itdawn.domain.entity.Article;
import com.itdawn.domain.entity.Category;
import com.itdawn.domain.vo.CategoryVo;
import com.itdawn.mapper.CategoryMapper;
import com.itdawn.service.ArticleService;
import com.itdawn.service.CategoryService;
import com.itdawn.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2024-03-12 06:25:51
 */

@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;

    @Override
    //查询分类列表的核心代码
    public ResponseResult getCategoryList() {
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        //要求查的是getStatus字段的值为1，注意SystemCanstants是我们写的一个常量类，用来解决字面值的书写问题
        articleWrapper.eq(Article::getStatus, SystemCanstants.ARTICLE_STATUS_NORMAL);
        //查询文章列表，条件就是上一行的articleWrapper
        List<Article> articleList = articleService.list(articleWrapper);
        //获取文章的分类id，并且去重。使用stream流来处理上一行得到的文章表集合
        Set<Long> categoryIds = articleList.stream()
                //下面那行可以优化为Lambda表达式+方法引用
                .map(new Function<Article, Long>() {
                    @Override
                    public Long apply(Article article) {
                        return article.getCategoryId();
                    }
                })
                //把文章的分类id转换成Set集合
                .collect(Collectors.toSet());


        //查询分类表
        List<Category> categories = listByIds(categoryIds);
        //注意SystemCanstants是我们写的一个常量类，用来解决字面值的书写问题
        categories = categories.stream()
                .filter(category -> SystemCanstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());

        //封装成CategoryVo实体类后返回给前端，CategoryVo的作用是只返回前端需要的字段。BeanCopyUtils是我们写的工具类
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);

        return ResponseResult.okResult(categoryVos);
    }

    //----------------------------后台系统写博客-查询文章分类的接口--------------------------------------

    @Override
    public List<CategoryVo> listAllCategory() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus, SystemCanstants.NORMAL);
        List<Category> list = list(queryWrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);
        return categoryVos;
    }
}
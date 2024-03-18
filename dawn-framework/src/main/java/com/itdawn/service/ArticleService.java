package com.itdawn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itdawn.domain.entity.Article;
import com.itdawn.domain.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public interface ArticleService extends IService<Article> {
    //文章列表
    ResponseResult hotArticleList();

    //分类查询文章列表
    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    //根据id查询文章详情
    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);
}

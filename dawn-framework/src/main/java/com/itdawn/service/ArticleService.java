package com.itdawn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itdawn.domain.entity.Article;
import com.itdawn.domain.ResponseResult;
import com.itdawn.domain.vo.ArticleByIdVo;
import com.itdawn.domain.vo.PageVo;
import com.itdawn.dto.AddArticleDto;
import com.itdawn.dto.ArticleDto;
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

    ResponseResult add(AddArticleDto article);

    PageVo selectArticlePage(Article article, Integer pageNum, Integer pageSize);

    ArticleByIdVo getInfo(Long id);

    void edit(ArticleDto article);
}

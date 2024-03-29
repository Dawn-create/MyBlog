package com.itdawn.controller;

import com.itdawn.annotation.SystemLog;
import com.itdawn.domain.ResponseResult;
import com.itdawn.service.ArticleService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
@Api(tags = "文章的相关接口文档", description = "我是描述信息")

public class ArticleController {
    @Autowired
    //注入公共模块的ArticleService接口
    private ArticleService articleService;

    //----------------------------------测试mybatisPlus---------------------------------

//    @GetMapping("/list")
//    //Article是公共模块的实体类
//    public List<Article> test(){
//        //查询数据库的所有数据
//        return articleService.list();
//    }

    //----------------------------------测试统一响应格式-----------------------------------
    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList() {
        //查询热门文章，封装成ResponseResult返回
        ResponseResult result = articleService.hotArticleList();
        return result;
    }

    //----------------------------------分页查询文章的列表---------------------------------


    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        return articleService.articleList(pageNum, pageSize, categoryId);
    }

    //------------------------------------查询文章详情------------------------------------

    @GetMapping("/{id}")
    //想要获取路径形式的参数而非query形式的，即article/3而不是article?3
    //需要增加一个@PathVariable注解
    public ResponseResult getArticleDetail(@PathVariable("id") Long id) {
        return articleService.getArticleDetail(id);
    }

    //------------------------------------增加浏览量------------------------------------

    @PutMapping("/updateViewCount/{id}")
    @SystemLog(businessName = "根据文章id从mysql查询文章")
    public ResponseResult updateViewCount(@PathVariable("id") Long id) {
        return articleService.updateViewCount(id);
    }
}

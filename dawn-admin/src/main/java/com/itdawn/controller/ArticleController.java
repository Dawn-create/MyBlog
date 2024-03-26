package com.itdawn.controller;

import com.itdawn.domain.ResponseResult;
import com.itdawn.domain.entity.Article;
import com.itdawn.domain.vo.ArticleByIdVo;
import com.itdawn.domain.vo.PageVo;
import com.itdawn.dto.AddArticleDto;
import com.itdawn.dto.ArticleDto;
import com.itdawn.service.ArticleService;
import com.itdawn.service.ArticleVoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping
    @PreAuthorize("@ps.hasPermission('content:article:writer')")
    public ResponseResult add(@RequestBody AddArticleDto article) {
        return articleService.add(article);
    }
    //-----------------------------分页查询博客文章---------------------------
    @GetMapping("/list")
    public ResponseResult list(Article article, Integer pageNum, Integer pageSize){
        PageVo pageVo = articleService.selectArticlePage(article,pageNum,pageSize);
        return ResponseResult.okResult(pageVo);
    }

    //---------------------------根据文章id来修改文章--------------------------

    @GetMapping(value = "/{id}")
    //①先查询根据文章id查询对应的文章
    public ResponseResult getInfo(@PathVariable(value = "id")Long id){
        ArticleByIdVo article = articleService.getInfo(id);
        return ResponseResult.okResult(article);
    }

    @PutMapping
    //②然后才是修改文章
    public ResponseResult edit(@RequestBody ArticleDto article){
        articleService.edit(article);
        return ResponseResult.okResult();
    }

    //---------------------------根据文章id来删除文章-------------------------

    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id){
        //直接使用mybatisplus提供的removeById方法
        articleService.removeById(id);
        return ResponseResult.okResult();
    }


}



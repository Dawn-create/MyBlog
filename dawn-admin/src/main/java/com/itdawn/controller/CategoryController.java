package com.itdawn.controller;

import com.itdawn.domain.ResponseResult;
import com.itdawn.domain.vo.CategoryVo;
import com.itdawn.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    //---------------------------写博文-查询文章分类的接口--------------------------------
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        List<CategoryVo> list=categoryService.listAllCategory();
        return ResponseResult.okResult(list);
    }
}

package com.itdawn.controller;

import com.itdawn.domain.ResponseResult;
import com.itdawn.domain.entity.Tag;
import com.itdawn.domain.vo.PageVo;
import com.itdawn.domain.vo.TagVo;
import com.itdawn.dto.AddTagDto;
import com.itdawn.dto.EditTagDto;
import com.itdawn.dto.TagListDto;
import com.itdawn.service.TagService;
import com.itdawn.utils.BeanCopyUtils;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    //查询标签列表功能
    @GetMapping("/list")
    //ResponseResult是我们在dawn-framework工程的实体类
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        return tagService.pageTagList(pageNum, pageSize, tagListDto);
    }

    //新增标签功能
    @PostMapping
    public ResponseResult add(@RequestBody AddTagDto addTagDto) {
        Tag tag = BeanCopyUtils.copyBean(addTagDto, Tag.class);
        tagService.save(tag);
        return ResponseResult.okResult();
    }

    //删除标签功能
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id) {
        tagService.removeById(id);
        return ResponseResult.okResult();
    }

    //修改标签功能
    //①根据标签的id来查询标签
    @GetMapping("/{id}")
    public ResponseResult getInfo(@PathVariable(value = "id") Long id){
        Tag tag=tagService.getById(id);
        return ResponseResult.okResult(tag);
    }

    @PutMapping
    //②根据标签的id来修改标签
    public ResponseResult edit(@RequestBody EditTagDto editTagDto){
        Tag tag=BeanCopyUtils.copyBean(editTagDto, Tag.class);
        tagService.updateById(tag);
        return ResponseResult.okResult();
    }
    //---------------写博文-查询文章标签---------------------------------
    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<TagVo> list=tagService.listAllTag();
        return ResponseResult.okResult(list);
    }



}

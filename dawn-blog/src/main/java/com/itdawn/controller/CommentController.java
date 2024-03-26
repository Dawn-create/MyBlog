package com.itdawn.controller;



import com.itdawn.constants.SystemCanstants;
import com.itdawn.domain.ResponseResult;
import com.itdawn.domain.entity.Comment;
import com.itdawn.service.CommentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@Api(tags = "评论的相关接口文档", description = "我是描述信息")
public class CommentController {
    @Autowired
    CommentService commentService;


    //文章评论列表
    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemCanstants.ARTICLE_COMMENT,articleId, pageNum, pageSize);
    }


    //发送评论
    @PostMapping
    public ResponseResult addComent(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }

    //获取友链评论
    @GetMapping("/linkCommentList")
    public ResponseResult linkCommentList(Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemCanstants.LINK_COMMENT, null, pageNum,pageSize);
    }
}

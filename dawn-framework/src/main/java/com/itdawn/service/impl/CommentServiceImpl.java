package com.itdawn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itdawn.constants.SystemCanstants;
import com.itdawn.domain.ResponseResult;
import com.itdawn.domain.entity.Comment;
import com.itdawn.domain.entity.User;
import com.itdawn.domain.vo.CommentVo;
import com.itdawn.domain.vo.PageVo;
import com.itdawn.enums.AppHttpCodeEnum;
import com.itdawn.exception.SystemException;
import com.itdawn.mapper.CommentMapper;
import com.itdawn.service.CommentService;
import com.itdawn.service.UserService;
import com.itdawn.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2024-03-14 17:14:25
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    private UserService userService;
    //敏感词列表
    private static final List<String> SENSITIVE_WORDS = Arrays.asList("shit", "fuck", "草", "妈的");

    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();

        //先判断是否为文章评论，因为友链评论是没有articleId的，并且常量要写在前面，否则如果commentType为null会出现空指针异常
        // 对articleId进行判断，作用是得到指定的文章
        queryWrapper.eq(SystemCanstants.ARTICLE_COMMENT.equals(commentType), Comment::getArticleId, articleId);

        //对评论区的某条评论的rootID进行判断，如果为-1，就表示是根评论。SystemCanstants是我们写的解决字面值的类
        queryWrapper.eq(Comment::getRootId, SystemCanstants.COMMENT_ROOT);

        //评论类型
        queryWrapper.eq(Comment::getType, commentType);

        //分页查询。查的是整个评论区的每一条评论
        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        //根评论排序
        //List<CommentVo> commentVoList = BeanCopyUtils.copyBeanList(page.getRecords(), CommentVo.class);
        List<Comment> sortedComment = page.getRecords().stream()
                .sorted(Comparator.comparing(Comment::getCreateTime).reversed())
                .collect(Collectors.toList());
        List<CommentVo> commentVoList = toCommentList(page.getRecords());

        //查询所有根评论对应的子评论集合，并且赋值给对应的属性，注意子评论只查到二级评论，不再往深查
        for (CommentVo commentVo : commentVoList) {
            //查询根评论对应的子评论
            List<CommentVo> children = getChildren(commentVo.getId());
            commentVo.setChildren(children);
        }
        return ResponseResult.okResult(new PageVo(commentVoList, page.getTotal()));
    }

    //-------------------------------发送评论--------------------------------------
    @Override
    public ResponseResult addComment(Comment comment) {
        //评论内容不能为空
        if (!StringUtils.hasText(comment.getContent())) {
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        } else if (containSensitiveWord(comment.getContent())) {
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_SENSITIVE);
        }
        save(comment);

        //封装响应返回
        return ResponseResult.okResult();
    }

    //-------------------------------commentList里调用的方法--------------------------------------


    //根据根评论的id，来查询对应的所有子评论
    public List<CommentVo> getChildren(Long id) {
        LambdaQueryWrapper<Comment> childWrapper = new LambdaQueryWrapper<>();
        childWrapper.eq(Comment::getRootId, id);
        List<Comment> comments = list(childWrapper);
        List<CommentVo> commentVos = toCommentList(comments);
        return commentVos;
    }

    //封装响应返回。CommentVo、BeanCopyUtils、ResponseResult、PageVo是我们写的类
    //封装响应返回。CommentVo、BeanCopyUtils、ResponseResult、PageVo是我们写的类
    private List<CommentVo> toCommentList(List<Comment> list) {
        //获取评论区的所有评论
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //遍历。由于封装响应好的数据里面没有username字段，所以我们还不能返回给前端。这个遍历就是用来得到username字段
        for (CommentVo commentVo : commentVos) {
//            //需要根据commentVo类里面的createBy字段，然后用createBy字段去查询user表的nickname字段(子评论的用户昵称)
//            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
//            //然后把nickname字段(发这条子评论的用户昵称)的数据赋值给commentVo类的username字段
//            commentVo.setUsername(nickName);
            // 获取评论创建者的昵称
            User user = userService.getById(commentVo.getCreateBy());
            if (user != null) {
                String nickName = user.getNickName();
                commentVo.setUsername(nickName);
            } else {
                // 处理user为null的情况，例如设置默认值或记录日志
                commentVo.setUsername("匿名用户"); // 或者其他合适的默认值
                // 也可以考虑记录一个警告日志
            }
            //查询根评论的用户昵称。怎么判断是根评论的用户呢，判断getToCommentUserId为1，就表示这条评论是根评论
//            if (commentVo.getToCommentUserId() != -1) {
//                String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
//                //然后把nickname字段(发这条根评论的用户昵称)的数据赋值给commentVo类的toCommentUserName字段
//                commentVo.setToCommentUserName(toCommentUserName);
//            }
            // 判断是否是根评论并获取根评论用户的昵称
            if (commentVo.getToCommentUserId() != -1) {
                User toCommentUser = userService.getById(commentVo.getToCommentUserId());
                if (toCommentUser != null) {
                    String toCommentUserName = toCommentUser.getNickName();
                    commentVo.setToCommentUserName(toCommentUserName);
                } else {
                    // 处理toCommentUser为null的情况
                    commentVo.setToCommentUserName("匿名用户"); // 或其他默认值
                }
            }
        }
        //返回给前端
        return commentVos;
    }

    //判断是否包含敏感词
    private boolean containSensitiveWord(String content) {
        // 将评论内容转换为小写，以便不区分大小写地进行检查
        for (String word : SENSITIVE_WORDS) {
            if (content.contains(word)) {
                return true;
            }
        }
        return false;
    }

}
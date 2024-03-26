package com.itdawn.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itdawn.domain.entity.ArticleTag;
import com.itdawn.mapper.ArticleTagMapper;
import com.itdawn.service.ArticleTagService;
import org.springframework.stereotype.Service;
/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2024-03-25 17:23:53
 */
@Service("articleTagService")

public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {
}


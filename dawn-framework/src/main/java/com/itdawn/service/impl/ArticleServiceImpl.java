package com.itdawn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itdawn.domain.entity.Article;
import com.itdawn.mapper.ArticleMapper;
import com.itdawn.service.ArticleService;
import org.springframework.stereotype.Service;

@Service
//ServiceImpl是mybatisPlus官方提供的
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

}


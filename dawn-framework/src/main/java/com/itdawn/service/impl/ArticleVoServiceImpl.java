package com.itdawn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itdawn.domain.ResponseResult;
import com.itdawn.domain.vo.ArticleVo;
import com.itdawn.dto.AddArticleDto;
import com.itdawn.mapper.ArticleVoMapper;
import com.itdawn.service.ArticleVoService;
import org.springframework.stereotype.Service;

@Service
public class ArticleVoServiceImpl extends ServiceImpl<ArticleVoMapper, ArticleVo> implements ArticleVoService {

}
package com.itdawn.runner;

import com.itdawn.domain.entity.Article;
import com.itdawn.mapper.ArticleMapper;
import com.itdawn.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//启动预处理

@Component
public class ViewCountRunner implements CommandLineRunner {

    //查询博客信息
    //存储到redis中
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        //查询博客信息   id:viewCount
        List<Article> articles = articleMapper.selectList(null);
        Map<String, Integer> viewCountMap = articles.stream()
                .collect(Collectors.toMap(article -> article.getId().toString(), article -> article.getViewCount().intValue()
                ));

        //把查询到的id作为key，viewCount作为value，一起存入Redis
        redisCache.setCacheMap("article: viewCount", viewCountMap);

    }
}

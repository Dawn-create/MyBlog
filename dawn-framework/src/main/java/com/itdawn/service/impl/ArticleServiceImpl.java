package com.itdawn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itdawn.constants.SystemCanstants;
import com.itdawn.domain.entity.Article;
import com.itdawn.domain.ResponseResult;
import com.itdawn.domain.entity.Category;
import com.itdawn.domain.vo.ArticleDetailVo;
import com.itdawn.domain.vo.ArticleListVo;
import com.itdawn.domain.vo.HotArticleVo;
import com.itdawn.domain.vo.PageVo;
import com.itdawn.mapper.ArticleMapper;
import com.itdawn.service.ArticleService;
import com.itdawn.service.CategoryService;
import com.itdawn.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
//ServiceImpl是mybatisPlus官方提供的
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {


    //----------------------------------查询热门文章---------------------------------

    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章，封装成ResponseResult返回
        //用来封装查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        /**
         需求：
         * 不能把草稿展示出来,必须是正式文章
         * 不能把删除了的文章查询出来，已经定义了逻辑删除字段，默认会加上，不用特别关注
         *
         * 最多只能查出来10条消息
         *实际开发时，都不允许直接在代码中使用字面值(代码中的固定值)。都需要定义成常量来使用。这种方式有利于提高代码的可维护性
         * */
        //不能把草稿展示出来,必须是正式文章
        //blog_article中定义：状态（0已发布，1草稿）
        //可以用 ("status",0) 的方式，但是存在风险，因为字符串不一定能完全匹配，最好用方法引用
        //queryWrapper.eq(Article::getStatus, 0);
        queryWrapper.eq(Article::getStatus, SystemCanstants.ARTICLE_STATUS_NORMAL);
        //要按照浏览量进行降序排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多只能查出来10条消息。可以进行分页查询，每页展示10条消息，只查第一页
        Page<Article> page = new Page<>(SystemCanstants.ARTICLE_STATUS_CURRENT, SystemCanstants.ARTICLE_STATUS_SIZE);
        page(page, queryWrapper);
        //获取最终的查询结果
        List<Article> articles = page.getRecords();
        //但是直接用list会把所有的字段都显示出来，所以要用VO进行优化
        //Bean拷贝
//        List<HotArticleVo> articleVos = new ArrayList<>();
//        for (Article article : articles) {
//            HotArticleVo vo = new HotArticleVo();
//            //此时vo中就具有article 的三个属性:id，title，viewCount
//            //字段的名字和类型一定要一致，否则拷贝不进去
//            BeanUtils.copyProperties(article, vo);
//            articleVos.add(vo);
//        }
        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
        return ResponseResult.okResult(hotArticleVos);

    }

    //----------------------------------分页查询文章的列表---------------------------------
    @Autowired
    //注入我们写的CategoryService接口
    private CategoryService categoryService;

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        /**
         * 需要查询所有的文章列表
         * 分类页面需要查询对应分类下的文章列表
         * 只能查询正式发布的文章
         * 置顶的文章要显示在最前面
         * **/
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //判空。如果 有categoryId就要查询时要和传入的相同。第二个参数是数据表的文章id，第三个字段是前端传来的文章id
        queryWrapper.eq(Objects.nonNull(categoryId) && categoryId > 0, Article::getCategoryId, categoryId);
        //只查询状态是正式发布的文章
        queryWrapper.eq(Article::getStatus, SystemCanstants.ARTICLE_STATUS_NORMAL);
        //对isTop字段进行降序排序，实现置顶的文章(isTop值为1)在最前面
        queryWrapper.orderByDesc(Article::getIsTop);
        //分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        //如果封装ArticleListVo后就直接PageVo会导致categoryName字段没有返回值，因此需要在分页之后，封装成ArticleListVo之前，进行处理
        //查询categoryName
        List<Article> articles = page.getRecords();
        //用categoryId来查询categoryName(category表的name字段)，也就是查询'分类名称'。
        //第一种方式，用for循环遍历的方式
//        for (Article article : articles) {
//            //'article.getCategoryId()'表示从article表获取category_id字段，然后作为查询category表的name字段
//            Category category =categoryService.getById(article.getCategoryId());
//            //把查询出来的category表的name字段值，也就是article，设置给Article实体类的categoryName成员变量
//            article.setCategoryName(category.getName());
//        }

        //第二种方法，用stream流的方式
        articles.stream()
                .map(new Function<Article, Article>() {
                    @Override
                    public Article apply(Article article){
                        //'article.getCategoryId()'表示从article表获取category_id字段，然后作为查询category表的name字段
                        Category category=categoryService.getById(article.getCategoryId());
                        String name = category.getName();
                        //把查询出来的category表的name字段值，也就是article，设置给Article实体类的categoryName成员变量
                        article.setCategoryName(category.getName());
                        //把查询出来的category表的name字段值，也就是article，设置给Article实体类的categoryName成员变量
                        return article;
                    }
                })
                .collect(Collectors.toList());
        


        //把最后的查询结果封装成ArticleListVo。
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);
        //把上面那行的查询结果和文章总数封装在PageVo(我们写的实体类)
        PageVo pageVo = new PageVo(articleListVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }


    //---------------------------------根据id查询文章详情---------------------------------

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //把最后的查询结果转换为Vo
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);

        //根据分类id，来查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);

        if(category!=null){
            articleDetailVo.setCategoryName(category.getName());
        }

        //封装响应返回。
        return ResponseResult.okResult(articleDetailVo);
    }
}


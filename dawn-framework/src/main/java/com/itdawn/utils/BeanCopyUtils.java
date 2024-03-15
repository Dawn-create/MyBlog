package com.itdawn.utils;

import com.itdawn.domain.vo.CategoryVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

//这个类用到很多泛型知识，可以对应去补一下
public class BeanCopyUtils {

    //私有的空参构造方法
    private BeanCopyUtils() {

    }

    //1.单个实体类的拷贝(暂时还用不上)。第一个参数是要拷贝的对象，第二个参数是类的字节码对象
    //使用方法泛型，可以直接返回传进去的类型
    public static <V> V copyBean(Object source, Class<V> clazz) {
        //创建目标对象
        Object result = null;
        try {
            result = clazz.newInstance();
            //实现属性拷贝,也就是把source的属性拷贝到这个目标对象。BeanUtils是spring提供的工具类
            BeanUtils.copyProperties(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回结果
        return (V) result;
    }

    //2.集合的拷贝(在ArticleServiceImpl类里面会使用到)。第一个参数是要拷贝的集合，第二个参数是类的字节码对象
    //注意<O,V>这里是字母O不是0
    public static <OB,V> List<V> copyBeanList(List<OB> sourceList,Class<V> clazz){
        //不使用for循环，使用stream流进行转换
        return sourceList.stream()
                .map(o -> copyBean(o, clazz))
                //把结果转换成集合
                .collect(Collectors.toList());
    }


//测试copyBean方法
//    public static void main(String[] args) {
//        Article article = new Article();
//        article.setId(1L);
//        article.setTitle("hello");
//
//        HotArticleVo hotArticleVo = copyBean(article, HotArticleVo.class);
//        System.out.println(hotArticleVo);
//
//    }

//    public static void main(String[] args) {
//        Category category=new Category();
//        category.setId(2L);
//        category.setName("java");
//
//        CategoryVo categoryVo=copyBean(category, CategoryVo.class);
//        System.out.println(categoryVo);
//    }
}

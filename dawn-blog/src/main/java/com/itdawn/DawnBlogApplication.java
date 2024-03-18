package com.itdawn;

import com.itdawn.domain.entity.Article;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/*
添加一个包扫描。不然注入不了公共模块的类(bean)
*/

@SpringBootApplication
@MapperScan("com.itdawn.mapper")
@EnableScheduling
public class DawnBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(DawnBlogApplication.class, args);
    }
}

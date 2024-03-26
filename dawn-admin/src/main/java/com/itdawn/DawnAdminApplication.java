package com.itdawn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.itdawn.mapper")
public class DawnAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(DawnAdminApplication.class, args);

    }
}

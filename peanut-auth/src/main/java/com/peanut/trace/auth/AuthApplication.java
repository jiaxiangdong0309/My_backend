package com.peanut.trace.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 认证授权模块启动类
 */
@SpringBootApplication(scanBasePackages = {"com.peanut.trace"})
@MapperScan("com.peanut.trace.common.mapper")
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}

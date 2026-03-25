package com.peanut.trace.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 产品信息模块启动类
 */
@SpringBootApplication(scanBasePackages = {"com.peanut.trace"})
@MapperScan("com.peanut.trace.common.mapper")
public class ProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
}

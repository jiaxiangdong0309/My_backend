package com.peanut.trace.gateway;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 花生产品溯源系统 - 网关主启动类
 * 集成所有模块：认证、产品、溯源、统计
 */
@SpringBootApplication(scanBasePackages = {"com.peanut.trace"})
@MapperScan("com.peanut.trace.common.mapper")
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
        System.out.println("\n" +
            "╔══════════════════════════════════════════╗\n" +
            "║   花生产品溯源系统 启动成功              ║\n" +
            "║   API文档: http://localhost:8080/doc.html ║\n" +
            "╚══════════════════════════════════════════╝"
        );
    }
}

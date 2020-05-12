package com.fengxuechao.example.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 用于测试灰度发布，服务分流等功能的服务
 *
 * @author fengxuechao
 * @date 2020/5/11
 */
@EnableConfigurationProperties(AppInfoProperties.class)
@SpringBootApplication
public class ApplicationNacos {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationNacos.class, args);
    }

}

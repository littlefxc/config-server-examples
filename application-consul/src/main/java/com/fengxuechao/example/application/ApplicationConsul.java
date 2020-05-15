package com.fengxuechao.example.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author fengxuechao
 * @date 2020/5/14
 */
@SpringBootApplication
public class ApplicationConsul {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ApplicationConsul.class);
        application.run(args);
    }
}

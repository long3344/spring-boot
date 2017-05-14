package com.wechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 描述：
 * 作者: TWL
 * 创建日期: 2017/5/10
 */
//@EnableConfigurationProperties(StorageProperties.class)
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages="com.wechat")
@EnableAsync
public class SpringBoot {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringBoot.class, args);
    }
}

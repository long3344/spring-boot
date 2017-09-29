package com.wechat;

import com.alibaba.druid.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public static final Logger logger = LoggerFactory.getLogger(SpringBoot.class);

    private static final String ENV_KEY_PROFILE = "spring.profiles.active";

    private static final String DEFAULT_PROFILE = "dev";

    public static void main(String[] args) throws Exception {
        SpringApplication application = new SpringApplication(SpringBoot.class);
        SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);
        if (!source.containsProperty(ENV_KEY_PROFILE) && !System.getenv().containsKey(ENV_KEY_PROFILE)
                && StringUtils.isEmpty(System.getProperty(ENV_KEY_PROFILE))) {
            logger.warn("未指定当前运行环境({}),使用默认环境[{}]", ENV_KEY_PROFILE, DEFAULT_PROFILE);
            application.setAdditionalProfiles(DEFAULT_PROFILE);
        }
        application.run(SpringBoot.class, args);
    }
}

package com.fengxuechao.example.application;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * AppInfo 配置信息
 *
 * @author fengxuechao
 * @date 2020/5/11
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "app.info")
public class AppInfoProperties {

    /**
     * 配置中心
     */
    private String configServer = "";

    /**
     * 应用名
     */
    private String appName = "";

    /**
     * 应用版本
     */
    private String appVersion = "0.0.1-SNAPSHOT";

    /**
     * 激活的Apollo命名空间
     */
    private String apolloNameSpaces = "application";
}

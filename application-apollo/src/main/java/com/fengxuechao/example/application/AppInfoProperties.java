package com.fengxuechao.example.application;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * AppInfo 配置信息
 *
 * @author fengxuechao
 * @date 2020/5/11
 */
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

    public String getConfigServer() {
        return configServer;
    }

    public void setConfigServer(String configServer) {
        this.configServer = configServer;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    @Override
    public String toString() {
        return "AppInfoProperties{" +
                "configServer='" + configServer + '\'' +
                ", appName='" + appName + '\'' +
                ", appVersion='" + appVersion + '\'' +
                '}';
    }
}

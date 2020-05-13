package com.fengxuechao.example.application;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author fengxuechao
 * @date 2020/5/13
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "other.info")
public class OtherInfoProperties {

    private String title;

    private String author;

    private String version;

}

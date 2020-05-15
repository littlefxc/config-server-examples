package com.fengxuechao.example.application;

import com.ecwid.consul.v1.ConsulClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.consul.ConditionalOnConsulEnabled;
import org.springframework.cloud.consul.ConsulAutoConfiguration;
import org.springframework.cloud.consul.config.ConfigWatch;
import org.springframework.cloud.consul.config.ConsulConfigProperties;
import org.springframework.cloud.endpoint.RefreshEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fengxuechao
 * @date 2020/5/14
 */
@Configuration
@ConditionalOnConsulEnabled
@ConditionalOnProperty(name = "spring.cloud.consul.config.enabled", matchIfMissing = true)
@AutoConfigureAfter(ConsulAutoConfiguration.class)
public class ConsulConfigVersionAutoConfig {

    @Bean("consulPropertyVersionSourceLocator")
    public ConsulPropertyVersionSourceLocator locator(
            ConsulClient consulClient, ConsulConfigProperties properties) {

        return new ConsulPropertyVersionSourceLocator(consulClient, properties);
    }

    @Bean("configVersionWatch")
    @ConditionalOnClass(RefreshEndpoint.class)
    @ConditionalOnProperty(name = "spring.cloud.consul.config.watch.enabled", matchIfMissing = true)
    public ConfigWatch configWatch(
            ConsulConfigProperties properties, ConsulPropertyVersionSourceLocator locator, ConsulClient consul) {

        return new ConfigWatch(properties, consul, locator.getContextIndexes());
    }
}

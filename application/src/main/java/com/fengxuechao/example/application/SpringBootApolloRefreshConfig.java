package com.fengxuechao.example.application;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author fengxuechao
 */
@Component
public class SpringBootApolloRefreshConfig {

    private static final Logger logger = LoggerFactory.getLogger(SpringBootApolloRefreshConfig.class);

    @Autowired
    private AppInfoProperties appInfoProperties;

    @Autowired
    private RefreshScope refreshScope;

    @Autowired
    private ApplicationContext applicationContext;

    @ApolloConfigChangeListener
    public void onChange(ConfigChangeEvent changeEvent) {
        logger.info("before refresh {}", appInfoProperties.toString());
        this.applicationContext.publishEvent(new EnvironmentChangeEvent(changeEvent.changedKeys()));
        logger.info("after refresh {}", appInfoProperties.toString());
    }

}
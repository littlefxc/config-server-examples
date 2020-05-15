package com.fengxuechao.example.application;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.GetValue;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.cloud.consul.config.ConsulConfigProperties;
import org.springframework.cloud.consul.config.ConsulFilesPropertySource;
import org.springframework.cloud.consul.config.ConsulPropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.retry.annotation.Retryable;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

import static org.springframework.cloud.consul.config.ConsulConfigProperties.Format.FILES;

/**
 * 自定义配置文件加载
 *
 * @author fengxuechao
 * @date 2020/5/14
 */
@Slf4j
//@Component
//@ConditionalOnConsulEnabled
public class ConsulPropertyVersionSourceLocator implements PropertySourceLocator, Ordered {

    private final ConsulClient consul;

    private final ConsulConfigProperties properties;

    private final List<String> contexts = new ArrayList<>();

    private final LinkedHashMap<String, Long> contextIndex = new LinkedHashMap<>();

    public ConsulPropertyVersionSourceLocator(ConsulClient consul, ConsulConfigProperties properties) {
        this.consul = consul;
        this.properties = properties;
    }

    @Deprecated
    public List<String> getContexts() {
        return contexts;
    }

    public LinkedHashMap<String, Long> getContextIndexes() {
        return contextIndex;
    }

    @Override
    @Retryable(interceptor = "consulRetryInterceptor")
    public PropertySource<?> locate(Environment environment) {
        if (environment instanceof ConfigurableEnvironment) {
            ConfigurableEnvironment env = (ConfigurableEnvironment) environment;
            RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(env);

            String appName = properties.getName();

            if (appName == null) {
                appName = propertyResolver.getProperty("spring.application.name");
            }

            // TODO 需要改为从consul上按需加载
            Integer expectedSize = propertyResolver.getProperty("spring.profiles.version", int.class, 1);
            List<String> profiles = Lists.newArrayListWithExpectedSize(expectedSize);
            for (int i = 0; i < expectedSize; i++) {
                profiles.add("version-" + i);
            }

            String prefix = this.properties.getPrefix();
            List<String> suffixes = new ArrayList<>();
            if (this.properties.getFormat() != FILES) {
                suffixes.add("/");
            } else {
                suffixes.add(".yml");
                suffixes.add(".yaml");
                suffixes.add(".properties");
            }

            String defaultContext = getContext(prefix, this.properties.getDefaultContext());

            // 只加载 application-version-${spring.profiles.version}.suffixes
            for (String suffix : suffixes) {
                addProfiles(this.contexts, defaultContext, profiles, suffix);
            }

            String baseContext = getContext(prefix, appName);

            // 只加载 ${spring.application.name}-version-${spring.profiles.version}.suffixes
            for (String suffix : suffixes) {
                addProfiles(this.contexts, baseContext, profiles, suffix);
            }

            Collections.reverse(this.contexts);

            CompositePropertySource composite = new CompositePropertySource("consul-version");

            for (String propertySourceContext : this.contexts) {
                try {
                    ConsulPropertySource propertySource = null;
                    if (this.properties.getFormat() == FILES) {
                        Response<GetValue> response = this.consul.getKVValue(propertySourceContext, this.properties.getAclToken());
                        addIndex(propertySourceContext, response.getConsulIndex());
                        if (response.getValue() != null) {
                            ConsulFilesPropertySource filesPropertySource = new ConsulFilesPropertySource(propertySourceContext, this.consul, this.properties);
                            filesPropertySource.init(response.getValue());
                            propertySource = filesPropertySource;
                        }
                    } else {
                        propertySource = create(propertySourceContext, contextIndex);
                    }
                    if (propertySource != null) {
                        composite.addPropertySource(propertySource);
                    }
                } catch (Exception e) {
                    if (this.properties.isFailFast()) {
                        log.error("Fail fast is set and there was an error reading configuration from consul.");
                        ReflectionUtils.rethrowRuntimeException(e);
                    } else {
                        log.warn("Unable to load consul config from " + propertySourceContext, e);
                    }
                }
            }

            return composite;
        }
        return null;
    }

    private String getContext(String prefix, String context) {
        if (StringUtils.isEmpty(prefix)) {
            return context;
        } else {
            return prefix + "/" + context;
        }
    }

    private void addIndex(String propertySourceContext, Long consulIndex) {
        contextIndex.put(propertySourceContext, consulIndex);
    }

    private ConsulPropertySource create(String context, Map<String, Long> contextIndex) {
        ConsulPropertySource propertySource = new ConsulPropertySource(context, this.consul, this.properties);
        propertySource.init();
        addIndex(context, propertySource.getInitialIndex());
        return propertySource;
    }

    private void addProfiles(List<String> contexts, String baseContext,
                             List<String> profiles, String suffix) {
        for (String profile : profiles) {
            contexts.add(baseContext + this.properties.getProfileSeparator() + profile + suffix);
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}

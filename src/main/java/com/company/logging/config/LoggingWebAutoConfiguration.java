package com.company.logging.config;

import com.company.logging.filter.LoggingFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(LoggingProperties.class)
public class LoggingWebAutoConfiguration {


    //log.trace.enabled=true
    @Bean
    @ConditionalOnProperty(
            prefix = "log.trace",
            name = "enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    public FilterRegistrationBean<LoggingFilter> loggingFilterRegistration(
            LoggingProperties properties
    ) {
        FilterRegistrationBean<LoggingFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new LoggingFilter(properties));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
        bean.addUrlPatterns("/*");
        return bean;
    }

}

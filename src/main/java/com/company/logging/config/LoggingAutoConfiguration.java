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
//log.trace.enabled=true
@ConditionalOnProperty(
        prefix = "log.trace",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class LoggingAutoConfiguration {

    @Bean
    public LoggingFilter loggingFilter(LoggingProperties properties){
        return new LoggingFilter(properties);
    }

    @Bean
    public FilterRegistrationBean<LoggingFilter> loggingFilter(LoggingFilter loggingFilter) {
        FilterRegistrationBean<LoggingFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(loggingFilter);
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
        bean.addUrlPatterns("/*");
        return bean;
    }

}

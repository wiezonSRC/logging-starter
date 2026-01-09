package com.company.logging.config;

import com.company.logging.sql.SqlTraceInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnClass(
        name = {
                "org.apache.ibatis.plugin.Interceptor",
                "org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration"
        }
)
@AutoConfigureAfter(
        name = "org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration"
)
@ConditionalOnProperty(
        prefix = "log.trace",
        name = "sql-enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class LoggingMybatisAutoConfiguration{
    @Bean
    public SqlTraceInterceptor sqlTraceInterceptor() {
        return new SqlTraceInterceptor();
    }

}

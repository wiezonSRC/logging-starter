package com.company.logging.config;
import com.company.logging.sql.SqlTraceInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;

@Configuration
@ConditionalOnClass(
        name = {
                "org.apache.ibatis.session.Configuration",
                "org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration"
        }
)
@ConditionalOnProperty(
        prefix = "log.trace",
        name = "sql-enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class LoggingMybatisAutoConfiguration {

    static {
        System.out.println(">>> LoggingMybatisAutoConfiguration LOADED");
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlTraceInterceptor sqlTraceInterceptor() {
        System.out.println(">>> SqlTraceInterceptor BEAN CREATED");
        return new SqlTraceInterceptor();
    }

    @Bean
    public ConfigurationCustomizer sqlTraceConfigurationCustomizer(
            SqlTraceInterceptor interceptor
    ) {
        System.out.println(">>> SqlTraceInterceptor ADD INTERCEPTOR");
        return configuration -> {
            configuration.addInterceptor(interceptor);
        };
    }
}
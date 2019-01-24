package com.proaim.crud.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:env/${spring.profiles.active}.properties")
@Data
public class AppConfigBean {
    @Value("${mysql.config.jdbc.driver.class.name}")
    private String driverClassName;
    @Value("${mysql.config.jdbc.url}")
    private String jdbcUrl;
    @Value("${mysql.config.jdbc.username}")
    private String jdbcUsername;
    @Value("${mysql.config.jdbc.password}")
    private String jdbcPassword;
}

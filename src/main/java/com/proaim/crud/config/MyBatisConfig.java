package com.proaim.crud.config;

import com.github.pagehelper.PageHelper;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
@Configuration
@MapperScan(value = "com.proaim.crud.service.mapper", sqlSessionFactoryRef = "sqlSessionFactory")
public class MyBatisConfig implements TransactionManagementConfigurer {
    @Autowired
    private AppConfigBean appConfigBean;

    @Primary
    @Bean(name = "realDataSource")
    DataSource realDataSource() throws SQLException {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(appConfigBean.getJdbcUrl());
        dataSource.setDriverClassName(appConfigBean.getDriverClassName());
        dataSource.setUsername(appConfigBean.getJdbcUsername());
        dataSource.setPassword(appConfigBean.getJdbcPassword());
        return dataSource;
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory getSqlSessionFactory(@Qualifier("realDataSource") DataSource dataSource) throws SQLException {

        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);

        // 分页插件
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("returnPageInfo", "check");
        properties.setProperty("params", "count=countSql");
        pageHelper.setProperties(properties);

        // 添加插件
        factoryBean.setPlugins(new Interceptor[]{pageHelper});

        // 添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            factoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis/mapper/*Mapper.xml"));
            factoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
            return factoryBean.getObject();
        } catch (Exception e) {
            log.warn("getSqlSessionFactory failed, errorMessage:{}", e);
            throw new RuntimeException(e);
        }
    }

    @Bean
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        try {
            return new DataSourceTransactionManager(realDataSource());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

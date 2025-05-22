package com.multitenancy.back.common.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class DataSourceConfig {

    final Environment env;

    @Autowired
    private DataSourceProperties properties;

    public DataSourceConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource dataSource() {
        MultiTenantDataSource multiTenantDataSource = new MultiTenantDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();

        // 테넌트 별 DB 설정
//        DataSource defaultDataSource = properties.initializeDataSourceBuilder().build();

//        targetDataSources.put("master", properties.initializeDataSourceBuilder().url(env.getProperty("spring.datasource.hikari.master.url")).username(env.getProperty("spring.datasource.hikari.master.username")).password(env.getProperty("spring.datasource.hikari.master.password")).build());
//        targetDataSources.put("tenant1", properties.initializeDataSourceBuilder().url(env.getProperty("spring.datasource.hikari.tenant1.url")).username(env.getProperty("spring.datasource.hikari.tenant1.username")).password(env.getProperty("spring.datasource.hikari.tenant1.password")).build());
//        targetDataSources.put("tenant2", properties.initializeDataSourceBuilder().url(env.getProperty("spring.datasource.hikari.tenant2.url")).username(env.getProperty("spring.datasource.hikari.tenant2.username")).password(env.getProperty("spring.datasource.hikari.tenant2.password")).build());

        // 1. createDataSource() 호출하여 테넌트별 DataSource 생성
        targetDataSources.put("master", createDataSource(env.getProperty("spring.datasource.hikari.master.url"), env.getProperty("spring.datasource.hikari.master.username"), env.getProperty("spring.datasource.hikari.master.password")));
        targetDataSources.put("tenant1", createDataSource(env.getProperty("spring.datasource.hikari.tenant1.url"), env.getProperty("spring.datasource.hikari.tenant1.username"), env.getProperty("spring.datasource.hikari.tenant1.password")));
        targetDataSources.put("tenant2", createDataSource(env.getProperty("spring.datasource.hikari.tenant2.url"), env.getProperty("spring.datasource.hikari.tenant2.username"), env.getProperty("spring.datasource.hikari.tenant2.password")));



        multiTenantDataSource.setTargetDataSources(targetDataSources);
        multiTenantDataSource.setDefaultTargetDataSource(targetDataSources.get("master")); // default

        return multiTenantDataSource;
    }

    private DataSource createDataSource(String url, String username, String password) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // Connection pool 설정
        dataSource.setMaximumPoolSize(30); // 동시 연결 개수
        dataSource.setMinimumIdle(5); // 최소 유휴 컨넥션 개수
        dataSource.setIdleTimeout(30000); // 커넥션 유지 시간
        dataSource.setMaxLifetime(1800000); // 연결 대기 시간 (ms)

        dataSource.setConnectionTimeout(30000); // 커넥션 타임아웃 시간
        dataSource.setValidationTimeout(5000); // 커넥션 검증 시간
        dataSource.setLeakDetectionThreshold(2000); // 커넥션 누수 탐지 시간
        dataSource.setAutoCommit(true); // 자동 커밋 여부

        return dataSource;
    }
}

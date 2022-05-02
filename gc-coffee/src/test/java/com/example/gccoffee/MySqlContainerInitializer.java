package com.example.gccoffee;

import com.example.gccoffee.repository.ProductJdbcRepository;
import com.example.gccoffee.repository.ProductRepository;
import com.zaxxer.hikari.HikariDataSource;
import org.assertj.core.internal.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringJUnitConfig
public class MySqlContainerInitializer {

    @Container
    protected static final MySQLContainer mySqlContainer = new MySQLContainer("mysql:8.0.19");

    @Configuration
    @ComponentScan(basePackages = "com.example.gccoffee")
    static class Config {

        @Bean
        public DataSource dataSource() {
            mySqlContainer.withInitScript("schema.sql").start();

            return DataSourceBuilder.create()
                    .driverClassName(mySqlContainer.getDriverClassName())
                    .url(mySqlContainer.getJdbcUrl())
                    .username(mySqlContainer.getUsername())
                    .password(mySqlContainer.getPassword())
                    .type(HikariDataSource.class)
                    .build();
        }

        @Bean
        public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
            return new NamedParameterJdbcTemplate(dataSource);
        }
    }
}

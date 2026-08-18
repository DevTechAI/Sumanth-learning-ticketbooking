package com.transaction.springtranscation.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class TransactionConfig
{
    @Bean(name = "transactionManager")
    @Primary
    public PlatformTransactionManager
    jpaTransactionManager(EntityManagerFactory entityManagerFactory)
    {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean(name = "jdbcTransactionManager")
    public PlatformTransactionManager
    jdbcTransactionManager(DataSource dataSource)
    {
        return new DataSourceTransactionManager(dataSource);
    }
}
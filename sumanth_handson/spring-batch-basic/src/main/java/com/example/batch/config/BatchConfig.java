package com.example.batch.config;

import com.example.batch.listener.JobCompletionListener;
import com.example.batch.model.Customer;
import com.example.batch.processor.CustomerProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.transaction.PlatformTransactionManager;
import javax.sql.DataSource;

@Configuration
public class BatchConfig {

    @Bean
    public FlatFileItemReader<Customer> customerReader() {
        return new FlatFileItemReaderBuilder<Customer>()
                .name("customerReader")
                .resource(new ClassPathResource("input/customers.csv"))
                .linesToSkip(1)
                .delimited()
                .names("id", "name", "email", "age")
                .targetType(Customer.class)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Customer> customerWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Customer>()
                .dataSource(dataSource)
                .sql("""
                        INSERT INTO customer (id, name, email, age)
                        VALUES (:id, :name, :email, :age)
                        ON CONFLICT (id) DO UPDATE SET
                          name = EXCLUDED.name, email = EXCLUDED.email, age = EXCLUDED.age
                        """)
                .itemSqlParameterSourceProvider(BeanPropertySqlParameterSource::new)
                .build();
    }

    @Bean
    public Step importCustomerStep(JobRepository jobRepository,
                                   PlatformTransactionManager transactionManager,
                                   FlatFileItemReader<Customer> customerReader,
                                   CustomerProcessor customerProcessor,
                                   JdbcBatchItemWriter<Customer> customerWriter) {
        return new StepBuilder("importCustomerStep", jobRepository)
                .<Customer, Customer>chunk(3, transactionManager)
                .reader(customerReader)
                .processor(customerProcessor)
                .writer(customerWriter)
                .build();
    }

    @Bean
    public Job customerImportJob(JobRepository jobRepository,
                                 Step importCustomerStep,
                                 JobCompletionListener listener) {
        return new JobBuilder("customerImportJob", jobRepository)
                .listener(listener)
                .start(importCustomerStep)
                .build();
    }
}

package com.sm.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.SimpleJobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.dao.*;
import org.springframework.batch.core.repository.support.AbstractJobRepositoryFactoryBean;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.core.repository.support.SimpleJobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ademus
 *         Date: 12/27/16
 */
@Configuration
public class CustomJobBatchConfiguration implements BatchConfigurer {

    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;
    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public CustomJobBatchConfiguration() throws Exception {
        this.transactionManager = new ResourcelessTransactionManager();
        JobInstanceDao jobInstanceDao = new MapJobInstanceDao();

        this.jobRepository = new SimpleJobRepository(jobInstanceDao,
                new MapJobExecutionDao(),
                new MapStepExecutionDao(),
                new MapExecutionContextDao()
        );

        SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setJobRepository(this.jobRepository);
        this.jobLauncher = simpleJobLauncher;
        this.jobExplorer = new SimpleJobExplorer(new MapJobInstanceDao(),
                new MapJobExecutionDao(),
                new MapStepExecutionDao(),
                new MapExecutionContextDao()
        );

        this.jobBuilderFactory = new JobBuilderFactory(this.jobRepository);
        this.stepBuilderFactory = new StepBuilderFactory(this.jobRepository, this.transactionManager);
    }

    public JobRepository getJobRepository() throws Exception {
        return jobRepository;
    }

    public PlatformTransactionManager getTransactionManager() throws Exception {
        return transactionManager;
    }

    public JobLauncher getJobLauncher() throws Exception {
        return jobLauncher;
    }

    public JobExplorer getJobExplorer() throws Exception {
        return jobExplorer;
    }

    @Bean
    public ItemReader<String> reader() {
        System.out.println("Read");
        return new ItemReader<String>() {
            public String read() throws Exception {
                return "1";
            }
        };
    }

    @Bean
    public ItemProcessor<Object, Integer> processor() {
        System.out.println("Process");

        return new ItemProcessor<Object, Integer>() {
            public Integer process(Object o) throws Exception {
                return 1;
            }
        };
    }

    @Bean
    public ItemWriter<Object> writer() {
        System.out.println("Write");

        return new ItemWriter<Object>() {
            public void write(List<?> list) throws Exception {
            }
        };
    }

    @Bean
    public Job userJob() {
        return jobBuilderFactory.get("userJob")
                .incrementer(new RunIdIncrementer())
                //.listener(listener)
                .flow(someStep())
                .end()
                .build();
    }

    @Bean
    public Step someStep() {
        return stepBuilderFactory.get("step1")
                .chunk(1)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
}
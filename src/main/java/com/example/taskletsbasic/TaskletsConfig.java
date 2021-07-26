package com.example.taskletsbasic;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@EnableBatchProcessing
public class TaskletsConfig {

    @Autowired private JobBuilderFactory jobs;
    @Autowired private StepBuilderFactory steps;
    @Autowired private DataSource dataSource;
    @Autowired private JobRepository jobRepository;

//    @Bean
//    public LinesReader linesReader() {
//        return new LinesReader();
//    }

//    @Bean
//    public LinesProcessor linesProcessor() {
//        return new LinesProcessor();
//    }

    @Bean
    @StepScope
    public Tasklet linesReader() {
        return (stepContribution, chunkContext) -> {
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    @StepScope
    public Tasklet linesProcessor(@Value("#{jobParameters['count']}") String count,
                                    @Value("#{jobParameters['name']}") String name) {
        return (stepContribution, chunkContext) -> {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            jdbcTemplate.update("insert into amal.users(count, name) VALUES(?, ?)", count, name);

            return RepeatStatus.FINISHED;
        };
    }


    @Bean
    protected Step readLines() {
        return steps
                .get("readLines")
                .tasklet(linesReader())
                .build();
    }

    @Bean
    protected Step processLines() {
        return steps
                .get("processLines")
                .tasklet(linesProcessor(null, null))
                .build();
    }

    @Bean
    public Job job() {
        return jobs
                .get("taskletsJob")
                .start(readLines())
                .next(processLines())
                .build();
    }

}
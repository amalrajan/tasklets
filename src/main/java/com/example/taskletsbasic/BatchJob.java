package com.example.taskletsbasic;

import com.example.taskletsbasic.batch.SimpleTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchJob {
    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    SimpleTasklet simpleTasklet;

    @Bean
    protected Step firstStep() {
        return steps
                .get("firstStep")
                .tasklet(simpleTasklet)
                .build();
    }

    @Bean(name = "SimpleJob")
    public Job job() {
        return jobs
                .get("job")
                .start(firstStep())
                .build();
    }

}
package com.example.taskletsbasic;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Calendar;

@EnableBatchProcessing
@SpringBootApplication
public class TaskletsBasicApplication implements CommandLineRunner {

    @Autowired
    @Qualifier("SimpleJobLauncher")
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("SimpleJob")
    private Job job;

    public static void main(String[] args) {
        SpringApplication.run(TaskletsBasicApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
            jobParametersBuilder.addDate("dt", Calendar.getInstance().getTime());
            JobExecution execution = jobLauncher.run(job, jobParametersBuilder.toJobParameters());

            System.out.println("Job Status : " + execution.getStatus());
            System.out.println("Job completed");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Job failed");
        }
    }

}

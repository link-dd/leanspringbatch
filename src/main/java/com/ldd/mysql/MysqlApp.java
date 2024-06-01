package com.ldd.mysql;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

//@SpringBootApplication
//@EnableBatchProcessing
public class MysqlApp {
    @Resource
    private JobLauncher jobLauncher;
    @Resource
    private JobBuilderFactory jobBuilderFactory;
    @Resource
    private StepBuilderFactory stepBuilderFactory;
    @Bean
    public Tasklet tasklet(){
        return (stepContribution, chunkContext)->{
            System.out.println("hello world");
            return RepeatStatus.FINISHED;
        };
    }
    @Bean
    public Step step(){
        return stepBuilderFactory.get("step")
                .tasklet(tasklet())
                .build();
    }
    @Bean
    public Job job(){
        return jobBuilderFactory.get("job")
                .start(step())
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(MysqlApp.class, args);
    }
}

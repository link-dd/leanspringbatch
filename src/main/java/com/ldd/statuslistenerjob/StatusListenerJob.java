package com.ldd.statuslistenerjob;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.listener.JobListenerFactoryBean;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import java.util.Map;

@EnableBatchProcessing
@SpringBootApplication
public class StatusListenerJob {
    @Resource
    private JobBuilderFactory jobBuilderFactory;
    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Tasklet tasklet() {
        return (stepContribution, chunkContext)->{
            JobExecution jobExecution = stepContribution.getStepExecution().getJobExecution();
            System.err.println("jobstatus is  = " + jobExecution.getStatus());
            return RepeatStatus.FINISHED;
        };
    }
//    @Bean
//    public JobStateListener listenerJob(){
//        return new JobStateListener();
//    }
    @Bean
    public Step step(){
        return stepBuilderFactory.get("step")
                .tasklet(tasklet())
                .build();
    }
    @Bean
    public Job job(){
        return jobBuilderFactory.get("job-status5")
                .start(step())
                .listener(JobListenerFactoryBean.getListener(new JobStateAnnoListener()))
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(StatusListenerJob.class, args);
    }
}

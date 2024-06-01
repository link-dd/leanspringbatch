package com.ldd.incrementParamJob;

import com.ldd.NameParamValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Hello world!
 */
@SpringBootApplication
@EnableBatchProcessing
public class IncrementParamJob {
    @Resource
    private JobLauncher jobLauncher;
    @Resource
    private JobBuilderFactory jobBuilderFactory;
    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Tasklet tasklet() {
        return (stepContribution, chunkContext) -> {
            Map<String, Object> jobName = chunkContext.getStepContext().getJobParameters();
            //  System.out.println("jobName = " + jobName.get("name"));
            System.out.println("job-run.id = " + jobName.get("run.id"));
            // System.out.println("jobAge = " + jobName.get("age"));
            return RepeatStatus.FINISHED;
        };
    }
//    @Bean
//    public DefaultJobParametersValidator defaultValidator(){
//        DefaultJobParametersValidator validator = new DefaultJobParametersValidator();
//        validator.setRequiredKeys(new String[]{"name"});
//        validator.setOptionalKeys(new String[]{"age"});
//        return validator;
//    }

    //    @StepScope
//    @Bean
//    public Tasklet tasklet(@Value("#{jobParameters['name']}")String name){
//        return (stepContribution, chunkContext) -> {
//
//            System.out.println(name);
//            return RepeatStatus.FINISHED;
//        };
//    }
    @Bean
    public DailyTimestampParamIncrementer dailyTimestampParamIncrementer() {
        return new DailyTimestampParamIncrementer();
    }
    @Bean
    public Step step() {
        return stepBuilderFactory.get("step1")
                .tasklet(tasklet())
                .build();
    }

    @Bean
    public NameParamValidator validator() {
        return new NameParamValidator();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("job-run-value")
                .incrementer(dailyTimestampParamIncrementer())
                .start(step()).build();
    }

    public static void main(String[] args) {
        SpringApplication.run(IncrementParamJob.class, args);
    }
}

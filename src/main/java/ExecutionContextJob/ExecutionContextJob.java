package ExecutionContextJob;

import com.ldd.statuslistenerjob.JobStateAnnoListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobListenerFactoryBean;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

@EnableBatchProcessing
@SpringBootApplication
public class ExecutionContextJob {
    @Resource
    private JobBuilderFactory jobBuilderFactory;
    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Tasklet tasklet() {
        return (stepContribution, chunkContext)->{
            StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
            ExecutionContext stepExecutionContext =stepExecution.getExecutionContext();
            stepExecutionContext.put("step1","ldd-step-value");
            stepExecution.getJobExecution().getExecutionContext().put("job1","ldd-job-value");
            return RepeatStatus.FINISHED;
        };
    }
    @Bean
    public Tasklet tasklet2() {
        return (stepContribution, chunkContext)->{
            StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
            ExecutionContext stepExecutionContext =stepExecution.getExecutionContext();
            System.err.println(stepExecutionContext.get("step1"));
            System.err.println(stepExecution.getJobExecution().getExecutionContext().get("job1"));
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
    public Step step2(){
        return stepBuilderFactory.get("step")
                .tasklet(tasklet2())
                .build();
    }
    @Bean
    public Job job(){
        return jobBuilderFactory.get("api-job-context")
                .start(step())
                .next(step2())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(ExecutionContextJob.class, args);
    }
}

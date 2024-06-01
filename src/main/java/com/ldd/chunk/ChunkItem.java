package com.ldd.chunk;

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
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

@EnableBatchProcessing
@SpringBootApplication
public class ChunkItem {
    @Resource
    private JobBuilderFactory jobBuilderFactory;
    @Resource
    private StepBuilderFactory stepBuilderFactory;
    int times = 4;
    @Bean
    public ItemReader<String> itemReader(){
        return ()->{

            if (times>0){
                System.out.println("Item Reader -->");
                times--;
                return "read return";
            }
          else {
              return null;
            }

        };
    }
    @Bean
    public ItemProcessor<String,String> itemProcessor(){
        return item->{
            System.out.println("Item Processor -->");
            return "process-ret" + item;
        };
    }
    @Bean
    public ItemWriter<String> itemWriter(){
        return items->{
            System.out.println("Item Writer -->"+items);
        };
    }


    @Bean
    public Step step(){
        return stepBuilderFactory.get("step")
                .<String, String>chunk(3)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public Job job(){
        return jobBuilderFactory.get("api-job-context")
                .start(step())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(ChunkItem.class, args);
    }
}

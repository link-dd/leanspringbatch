package com.ldd.statuslistenerjob;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;

public class JobStateAnnoListener {
    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        System.err.println("annobeforeJob = " + jobExecution.getStatus());
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        System.err.println("annoafterJob = " + jobExecution.getStatus());
    }
}

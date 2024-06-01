package com.ldd.statuslistenerjob;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class JobStateListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.err.println("beforeJob = " + jobExecution.getStatus());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.err.println("afterJob = " + jobExecution.getStatus());
    }
}

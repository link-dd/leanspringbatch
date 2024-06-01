package com.ldd.incrementParamJob;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

import java.util.Date;

public class DailyTimestampParamIncrementer  implements JobParametersIncrementer {
    @Override
    public JobParameters getNext(JobParameters jobParameters) {

            return new JobParametersBuilder(jobParameters).addLong("date", new Date().getTime())
                    .toJobParameters();

    }
}

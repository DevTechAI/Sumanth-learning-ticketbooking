package com.example.batch.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
public class BatchController {
    private final JobLauncher jobLauncher;
    private final Job customerImportJob;

    public BatchController(JobLauncher jobLauncher, @Qualifier("customerImportJob") Job customerImportJob) {
        this.jobLauncher = jobLauncher;
        this.customerImportJob = customerImportJob;
    }

    @PostMapping("/run")
    public String runJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("runId", System.currentTimeMillis())
                .toJobParameters();

        JobExecution execution = jobLauncher.run(customerImportJob, params);
        return "Job started. Execution Id=" + execution.getId() + ", Status=" + execution.getStatus();
    }
}

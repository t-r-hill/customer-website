package co.LabsProjects.customerwebsite.service;

import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class JobService {

    @Autowired
    JobExplorer jobExplorer;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;

    public List<JobExecution> getAllJobExecutions(){
        List<JobInstance> jobInstances = jobExplorer.getJobInstances("customer-loader-job", 0, 100);
        if (jobInstances.isEmpty()){
            return new ArrayList<JobExecution>();
        }
        return jobExplorer.getJobExecutions(jobInstances.get(0));
    }

    public void runJob(Resource resource) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException, IOException {
        // Run batch job
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addString("csvUpload", resource.getURI().toString());
        jobParametersBuilder.addDate("now", Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        JobExecution jobExecution;
        jobExecution = jobLauncher.run(job, jobParametersBuilder.toJobParameters());
    }

}

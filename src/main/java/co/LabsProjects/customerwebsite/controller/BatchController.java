package co.LabsProjects.customerwebsite.controller;

import co.LabsProjects.customerwebsite.exception.IdNotFoundException;
import co.LabsProjects.customerwebsite.service.JobService;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
public class BatchController {

    @Autowired
    JobService jobService;


    @GetMapping("/upload")
    public String showCustomerUploadPage(Model model) {
        List<JobExecution> jobExecutions = jobService.getAllJobExecutions();
        model.addAttribute("jobExecutions", jobExecutions);
        return "batch-jobs";
    }

    @PostMapping("/upload")
    public String uploadFileAndRunBatchJob(@RequestParam("csvUpload") MultipartFile csvUpload) throws IOException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        //Save multipartFile file in a temporary physical folder
        String path = new ClassPathResource("tmpuploads/").getURL().getPath();
        File fileToImport = new File(path + csvUpload.getOriginalFilename());

//        Resource resource = new InputStreamResource(csvUpload.getInputStream());

        jobService.runJob(fileToImport.getAbsolutePath());
        return "redirect:/upload";
    }

    @ExceptionHandler(JobExecutionException.class)
    public String handleBindException(JobExecutionException ex, Model model) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        errors.put("stacktrace", Arrays.toString(ex.getStackTrace()));
        model.addAttribute("errors",errors);
        return "error-page";
    }
}

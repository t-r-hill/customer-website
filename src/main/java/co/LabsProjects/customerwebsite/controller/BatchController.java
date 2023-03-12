package co.LabsProjects.customerwebsite.controller;

import co.LabsProjects.customerwebsite.service.BatchService;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
public class BatchController {

    @Autowired
    BatchService batchService;


    @GetMapping("/upload")
    public String showCustomerUploadPage(Model model) {
        List<JobExecution> jobExecutions = batchService.getAllJobExecutions();
        model.addAttribute("jobExecutions", jobExecutions);
        return "batch-jobs";
    }

    @PostMapping("/upload")
    public String uploadFileAndRunBatchJob(@RequestParam("csvUpload") MultipartFile csvUpload) throws IOException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        //Save multipartFile file in a temporary physical folder
        String path = new ClassPathResource("tmpuploads/").getURL().getPath();
        File fileToImport = new File(path + csvUpload.getOriginalFilename());
        csvUpload.transferTo(fileToImport);

//        Resource resource = new InputStreamResource(csvUpload.getInputStream());

        batchService.runJob(fileToImport.getAbsolutePath());
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

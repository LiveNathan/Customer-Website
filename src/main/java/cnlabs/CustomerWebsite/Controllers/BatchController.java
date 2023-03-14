package cnlabs.CustomerWebsite.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
public class BatchController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @GetMapping("/batch/job")
    public String testJob(Model model) {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"));
        jobParametersBuilder.addString("timestamp", timestamp);  // Add the timestamp to the job parameters to be used by the job.
        JobExecution jobExecution;
        try {
            jobExecution = jobLauncher.run(job, jobParametersBuilder.toJobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            e.printStackTrace();
            return e.getMessage();
        }

        model.addAttribute("fileName", jobExecution.getExecutionContext().getString("outputFileName"));
        model.addAttribute("jobExecutionID", jobExecution.getJobId().toString());  // I could probably just send the jobExecution object instead, but from things I saw on the internet, it looked like that would be complicated.
        model.addAttribute("jobExecutionStatus", jobExecution.getStatus().toString());

        return "dashboard";

    }

    @Value("${output.directory}") // assuming the output directory is configured in the application.properties file
    private String outputDirectory;

    @GetMapping("/batch/job/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String fileName) throws IOException {
        Path filePath = Path.of(outputDirectory, fileName);
        if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
            FileSystemResource fileSystemResource = new FileSystemResource(filePath);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);  // This seems to be the correct way to set the content type.
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentLength(fileSystemResource.contentLength());
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileSystemResource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
//@RequestMapping("/batch")
@RequiredArgsConstructor
public class BatchController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @GetMapping(value = "/batch/job")
    public String testJob(Model model) {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"));
        jobParametersBuilder.addString("timestamp", timestamp);
        JobExecution jobExecution;
        try {
            jobExecution = jobLauncher.run(job, jobParametersBuilder.toJobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
            return e.getMessage();
        }

        String filepath = "~/output/outputData-" + timestamp + ".csv";
        model.addAttribute("filepath", filepath);
        model.addAttribute("jobExecutionID", jobExecution.getJobId().toString());
        model.addAttribute("jobExecutionStatus", jobExecution.getStatus().toString());

        return "dashboard";

    }
}

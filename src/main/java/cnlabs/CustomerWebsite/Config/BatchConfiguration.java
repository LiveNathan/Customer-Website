package cnlabs.CustomerWebsite.Config;

import cnlabs.CustomerWebsite.Models.Customer;
import cnlabs.CustomerWebsite.Repos.CustomerRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    // Job
    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory, Step step1) {
        return jobBuilderFactory.get("customer-loader-job")
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }


    // Step
    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<Customer> repositoryReader, DateProcessor processor, CustomerWriter writer) {
        return stepBuilderFactory.get("step-1")
                .<Customer, Customer>chunk(100)
                .reader(repositoryReader)      // EXTRACT
                .processor(processor)   // TRANSFORM
                .writer(writer)         // LOAD
                .build();
    }

    // ItemReader
    @Bean
    public RepositoryItemReader<Customer> repositoryReader(CustomerRepository customerRepository) {
        return new RepositoryItemReaderBuilder<Customer>()
                .repository(customerRepository)
                .methodName("findAll")
                .sorts(Map.of("id", Sort.Direction.ASC))
                .name("repository-reader")
                .build();
    }

    // ItemProcessor
    @Component
    public static class DateProcessor implements ItemProcessor<Customer, Customer> {
        @Override
        public Customer process(Customer customer) {
            if (customer.getBookCheckoutDate() != null) {
                if (customer.getBookCheckoutDate().plusDays(14).isBefore(LocalDate.now())) {  // if checkout date + 14 < today
                    return customer;  // return the customer
                }
            }
            return null;  // To filter a record, you can return null from the ItemProcessor.
        }
    }

    // ItemWriter
    @Component
    public static class CustomerWriter implements ItemWriter<Customer> {

        private Resource outputResource;
        private FlatFileItemWriter<Customer> writer;

        @BeforeStep
        public void beforeStep(final StepExecution stepExecution) throws Exception {
            JobParameters parameters = stepExecution.getJobExecution().getJobParameters();
            String timestamp = parameters.getString("timestamp");
//            System.out.println(timestamp);
            outputResource = new FileSystemResource("src/main/resources/templates/output/outputData-" + timestamp + ".csv");
            writer = new FlatFileItemWriter<>();

            // Set output file location
            writer.setResource(outputResource);

            // All job repetitions should "append" to same output file
            writer.setAppendAllowed(true);

            // Name field values sequence based on object properties
            writer.setLineAggregator(new DelimitedLineAggregator<Customer>() {
                {
                    setDelimiter(",");
                    setFieldExtractor(new BeanWrapperFieldExtractor<Customer>() {
                        {
                            setNames(new String[]{"id", "fullName", "emailAddress", "age", "address", "book"});
                        }
                    });
                }
            });

            // Open the writer
            writer.open(new ExecutionContext());
            stepExecution.getJobExecution().getExecutionContext().putString("outputFileName", outputResource.getFile().getName());  // Store the file name to the job execution context.
        }

        @Override
        public void write(List<? extends Customer> customers) throws Exception {
            writer.write(customers);
            System.out.println("Saved customers: " + customers);
        }

        @AfterStep
        public void afterStep() throws Exception {
            // Close the writer
            writer.close();
        }
    }


}

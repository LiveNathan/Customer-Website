package cnlabs.CustomerWebsite.Config;

import cnlabs.CustomerWebsite.Models.Customer;
import cnlabs.CustomerWebsite.Repos.CustomerRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Sort;

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
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<Customer> repositoryReader, NameProcessor processor, CustomerWriter writer) {
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
    public static class NameProcessor implements ItemProcessor<Customer, Customer> {
        @Override
        public Customer process(Customer customer) {
//            customer.setName(customer.getName().toUpperCase());
//            customer.setNameUpdatedAt(new Date());
            return customer;
        }
    }

    // ItemWriter
    @Component
    public static class CustomerWriter implements ItemWriter<Customer> {

        @Autowired
        private CustomerRepository customerRepository;

        @Override
        public void write(List<? extends Customer> employees) throws InterruptedException {
            customerRepository.saveAll(employees);
            System.out.println("Saved employees: " + employees);
        }
    }
}

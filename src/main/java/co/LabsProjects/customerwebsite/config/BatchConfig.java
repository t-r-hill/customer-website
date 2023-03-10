package co.LabsProjects.customerwebsite.config;

import co.LabsProjects.customerwebsite.model.Customer;
import co.LabsProjects.customerwebsite.repo.CustomerRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory, Step step) {
        return jobBuilderFactory.get("customer-loader-job")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public Step customerImportStep(StepBuilderFactory stepBuilderFactory, ItemReader<Customer> customerCsvReader,
                                   CustomerAddressProcessor processor, CustomerWriter writer){
        return stepBuilderFactory.get("customer-import-step")
                .<Customer, Customer> chunk(10)
                .reader(customerCsvReader)
                .processor(processor)
                .writer(writer)
                .allowStartIfComplete(false)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Customer> customerCsvReader(@Value("#{jobParameters[csvUpload]}") Resource resource) throws IOException {
        return new FlatFileItemReaderBuilder<Customer>()
                .name("customer-csv-reader")
                .resource(resource)
                .delimited()
                .names("first_name", "last_name","email", "address", "age")
                .linesToSkip(1)
                .fieldSetMapper(new CustomerFieldSetMapper())
                .build();
    }

    @Component
    public static class CustomerFieldSetMapper implements FieldSetMapper<Customer>{

        @Override
        public Customer mapFieldSet(FieldSet fieldSet) throws BindException {
            Customer customer = Customer.builder()
                    .age(fieldSet.readInt("age"))
                    .address(fieldSet.readString("address"))
                    .emailAddress(fieldSet.readString("email"))
                    .fullName(fieldSet.readString("first_name").concat(" ").concat(fieldSet.readString("last_name")))
                    .build();
            return customer;
        }
    }

    @Component
    public static class CustomerAddressProcessor implements ItemProcessor<Customer,Customer>{

        @Override
        public Customer process(Customer customer) throws Exception {
            customer.setAddress(customer.getAddress().concat(" UK"));
            return customer;
        }
    }

    @Component
    public static class CustomerWriter implements ItemWriter<Customer>{

        @Autowired
        CustomerRepository customerRepository;

        @Override
        public void write(List<? extends Customer> customers) throws Exception {
            customerRepository.saveAll(customers);
        }
    }
}

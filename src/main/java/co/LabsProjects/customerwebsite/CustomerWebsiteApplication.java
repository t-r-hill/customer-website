package co.LabsProjects.customerwebsite;

import co.LabsProjects.customerwebsite.model.Customer;
import co.LabsProjects.customerwebsite.model.Role;
import co.LabsProjects.customerwebsite.model.User;
import co.LabsProjects.customerwebsite.repo.CustomerRepository;
import co.LabsProjects.customerwebsite.repo.RoleRepository;
import co.LabsProjects.customerwebsite.repo.UserRepository;
import co.LabsProjects.customerwebsite.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
public class CustomerWebsiteApplication implements CommandLineRunner {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private PasswordEncoder encoder;

	// The main method is defined here which will start your application
	public static void main(String[] args) {
		SpringApplication.run(CustomerWebsiteApplication.class);
	}

	// You can also define a run method which performs an operation at runtime
	// In this example, the run method saves some Customer data into the database for testing
	@Override
	public void run(String... args) throws Exception {

		Role userRole = new Role(Role.Roles.USER_ROLE);
		Role adminRole = new Role(Role.Roles.ADMIN_ROLE);

		if (roleRepository.findAll().isEmpty()){
			roleRepository.saveAll(Arrays.asList(userRole, adminRole));
		}

		Customer customer1 = Customer.builder()
				.fullName("Customer 1")
				.emailAddress("customer1@gmail.com")
				.address("Customer Address One")
				.age(30)
				.build();

		if (customerRepository.findAll().isEmpty()){
			customerRepository.save(customer1);
		}

		User adminUser = new User("adminUser1",encoder.encode("password1"),Collections.singletonList(adminRole));
		User customerUser = new User("customer1", encoder.encode("password1"),Collections.singletonList(userRole),customer1);

		if (userRepository.findAll().isEmpty()){
			userRepository.saveAll(Arrays.asList(adminUser, customerUser));
		}

	}
}

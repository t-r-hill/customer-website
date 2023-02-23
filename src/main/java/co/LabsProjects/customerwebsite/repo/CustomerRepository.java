package co.LabsProjects.customerwebsite.repo;

import co.LabsProjects.customerwebsite.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}

package co.LabsProjects.customerwebsite.repo;

import co.LabsProjects.customerwebsite.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    public List<Subscription> getByCustomerIsNull();
}

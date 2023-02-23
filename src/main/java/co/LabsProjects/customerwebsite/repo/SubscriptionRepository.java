package co.LabsProjects.customerwebsite.repo;

import co.LabsProjects.customerwebsite.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
}

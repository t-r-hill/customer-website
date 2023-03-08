package co.LabsProjects.customerwebsite.service;

import co.LabsProjects.customerwebsite.model.Customer;
import co.LabsProjects.customerwebsite.model.Subscription;
import co.LabsProjects.customerwebsite.repo.CustomerRepository;
import co.LabsProjects.customerwebsite.repo.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    CustomerService customerService;


    public Subscription createNewSubscription(Subscription subscription){
        return subscriptionRepository.save(subscription);
    }

    public List<Subscription> getAllSubscriptions(){
        return subscriptionRepository.findAll();
    }

    public List<Subscription> getAvailableSubscriptions(){
        return subscriptionRepository.getByCustomerIsNull();
    }

    public Subscription getSubscription(Long id){
        return subscriptionRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteSubscription(Long id) {
        Subscription subscription = getSubscription(id);
        Customer customer = subscription.getCustomer();
        customer.setSubscription(null);
        customerService.saveCustomer(customer);
        subscriptionRepository.deleteById(id);
    }
}

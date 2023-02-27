package co.LabsProjects.customerwebsite.service;

import co.LabsProjects.customerwebsite.model.Subscription;
import co.LabsProjects.customerwebsite.repo.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;


    public Subscription createNewSubscription(Subscription subscription){
        return subscriptionRepository.save(subscription);
    }

    public List<Subscription> getAllSubscriptions(){
        return subscriptionRepository.findAll();
    }

    public List<Subscription> getAvailableSubscriptions(){
        return subscriptionRepository.getByCustomerIsNull();
    }
}

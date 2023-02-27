package co.LabsProjects.customerwebsite.controller;

import co.LabsProjects.customerwebsite.model.Subscription;
import co.LabsProjects.customerwebsite.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/subscription")
public class SubscriptionController {

    @Autowired
    SubscriptionService subscriptionService;

    @GetMapping("/new")
    public String showNewSubscriptionPage(Model model){
        Subscription subscription = new Subscription();
        model.addAttribute(subscription);
        return "new-subscription";
    }

    @GetMapping
    public String showAllSubscriptions(Model model){
        List<Subscription> subscriptionList = subscriptionService.getAllSubscriptions();
        model.addAttribute(subscriptionList);
        return "subscription-index";
    }

    @PostMapping("/save-new")
    public String addNewSubscription(@ModelAttribute("subscription") @Valid Subscription subscription){
        subscriptionService.createNewSubscription(subscription);
        return "redirect:/subscription";
    }


}

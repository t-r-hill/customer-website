package co.LabsProjects.customerwebsite.controller;

import co.LabsProjects.customerwebsite.exception.IdNotFoundException;
import co.LabsProjects.customerwebsite.model.Customer;
import co.LabsProjects.customerwebsite.model.Subscription;
import co.LabsProjects.customerwebsite.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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

    @PostMapping("/update/{id}")
    public String updateSubscription(@PathVariable(name = "id") Long id,
                                 @ModelAttribute("subscription") @Valid Subscription subscription,
                                 Model model) throws IdNotFoundException {
        if (!id.equals(subscription.getId())) {
            throw new IdNotFoundException("Subscription with id - " + subscription.getId() + " - does not match id - " + id);
        }
        subscriptionService.createNewSubscription(subscription);
        return "redirect:/subscription/";
    }

    @GetMapping("/edit/{id}")
    // The path variable "id" is used to pull a customer from the database
    public ModelAndView showEditSubscriptionPage(@PathVariable(name = "id") Long id) throws IdNotFoundException {
        // Since the previous methods use Model, this one uses ModelAndView
        // to get some experience using both. Model is more common these days,
        // but ModelAndView accomplishes the same thing and can be useful in
        // certain circumstances. The view name is passed to the constructor.
        ModelAndView mav = new ModelAndView("edit-subscription");
        Subscription subscription = subscriptionService.getSubscription(id);
        if (subscription == null){
            throw new IdNotFoundException("A subscription with id " + id + " does not exist");
        }
        mav.addObject("subscription", subscription);
        return mav;
    }

    @GetMapping("/delete/{id}")
    public String deleteSubscription(@PathVariable(name = "id") Long id) throws IdNotFoundException {
        if (subscriptionService.getSubscription(id) == null){
            throw new IdNotFoundException("A customer with id " + id + " does not exist");
        }
        subscriptionService.deleteSubscription(id);
        return "redirect:/subscription/";
    }


}

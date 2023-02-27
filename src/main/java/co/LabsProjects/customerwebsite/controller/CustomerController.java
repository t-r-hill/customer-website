package co.LabsProjects.customerwebsite.controller;

import co.LabsProjects.customerwebsite.exception.IdNotFoundException;
import co.LabsProjects.customerwebsite.model.Customer;
import co.LabsProjects.customerwebsite.model.Subscription;
import co.LabsProjects.customerwebsite.service.CustomerService;
import co.LabsProjects.customerwebsite.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.management.InstanceNotFoundException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Autowired
    SubscriptionService subscriptionService;

    @GetMapping("/")
    public String viewHomePage(Model model) {
        // Here you call the service to retrieve all the customers
        final List<Customer> customerList = customerService.getAllCustomers();
        // Once the customers are retrieved, you can store them in model and return it to the view
        model.addAttribute("customerList", customerList);
        return "index";
    }

    @GetMapping("/new")
    public String showNewCustomerPage(Model model) {
        // Here a new (empty) Customer is created and then sent to the view
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        return "new-customer";
    }

    @PostMapping(value = "/save")
    // As the Model is received back from the view, @ModelAttribute
    // creates a Customer based on the object you collected from
    // the HTML page above
    public String saveCustomer(@ModelAttribute("customer") @Valid Customer customer) {
        customerService.saveCustomer(customer);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    // The path variable "id" is used to pull a customer from the database
    public ModelAndView showEditCustomerPage(@PathVariable(name = "id") Long id) throws IdNotFoundException {
        // Since the previous methods use Model, this one uses ModelAndView
        // to get some experience using both. Model is more common these days,
        // but ModelAndView accomplishes the same thing and can be useful in
        // certain circumstances. The view name is passed to the constructor.
        ModelAndView mav = new ModelAndView("edit-customer");
        Customer customer = customerService.getCustomer(id);
        if (customer == null){
            throw new IdNotFoundException("A customer with id " + id + " does not exist");
        }
        mav.addObject("customer", customer);
        return mav;
    }

    @GetMapping("/assign-subscription/{id}")
    public String showAssignSubscriptionPage(@PathVariable long id, Model model) throws IdNotFoundException{
        Customer customer = customerService.getCustomer(id);
        if (customer == null){
            throw new IdNotFoundException("A customer with id " + id + " does not exist");
        } else if (customer.getSubscription() != null) {
            throw new IdNotFoundException("Customer with id " + id + "already has a subscription assigned");
        }
        model.addAttribute("customer", customer);
        // Get subscriptions which haven't been assigned and add to model
        List<Subscription> subscriptionList = subscriptionService.getAvailableSubscriptions();
        model.addAttribute("subscriptionList", subscriptionList);
        return "assign-subscription";
    }

    @PostMapping("/assign-subscription")
    public String saveAssignedSubscription(@ModelAttribute("customer") Customer customer){
        customerService.saveCustomer(customer);
        return "redirect:/";
    }

    @RequestMapping("/remove-subscription/{id}")
    public String removeAssignedSubscription(@PathVariable Long id) throws IdNotFoundException{
        Customer customer = customerService.getCustomer(id);
        if (customer == null){
            throw new IdNotFoundException("A customer with id " + id + " does not exist");
        } else if (customer.getSubscription() == null){
            throw new IdNotFoundException("Customer doesn't have a subscription assigned");
        }
        customer.setSubscription(null);
        customerService.saveCustomer(customer);
        return "redirect:/";
    }

    @PostMapping("/update/{id}")
    public String updateCustomer(@PathVariable(name = "id") Long id,
                                 @ModelAttribute("customer") @Valid Customer customer,
                                 Model model) throws IdNotFoundException {
        if (!id.equals(customer.getId())) {
            throw new IdNotFoundException("Customer with id - " + customer.getId() + " - does not match id - " + id);
        }
        customerService.saveCustomer(customer);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable(name = "id") Long id) throws IdNotFoundException {
        if (customerService.getCustomer(id) == null){
            throw new IdNotFoundException("A customer with id " + id + " does not exist");
        }
        customerService.deleteCustomer(id);
        return "redirect:/";
    }

    @ExceptionHandler(BindException.class)
    public String handleBindException(BindException ex, Model model) {
        List<String> errors = new ArrayList();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.add(error.getDefaultMessage()));

        model.addAttribute("errors",errors);

        return "error-page";
    }

    @ExceptionHandler(IdNotFoundException.class)
    public String handleInstanceNotFound(InstanceNotFoundException ex, Model model){
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());
        model.addAttribute("errors", errors);
        return "error-page";
    }
}

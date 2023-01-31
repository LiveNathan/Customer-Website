package cnlabs.CustomerWebsite.Controllers;

import cnlabs.CustomerWebsite.Models.Customer;
import cnlabs.CustomerWebsite.Services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CustomerController {
    @Autowired
    private final CustomerService customerService;

    @GetMapping("/")
    public String viewHomePage(Model model) {
        // Here you call the service to retrieve all the customers
        final List<Customer> customerList = customerService.getAllCustomers();
        // Once the customers are retrieved, you can store them in model and return it to the view
        model.addAttribute("customerList", customerList);
        return "index";
    }
}

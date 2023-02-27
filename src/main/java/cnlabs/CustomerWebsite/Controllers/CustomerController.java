package cnlabs.CustomerWebsite.Controllers;

import cnlabs.CustomerWebsite.Models.Customer;
import cnlabs.CustomerWebsite.Services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CustomerController {
    @Autowired
    private final CustomerService customerService;

    @GetMapping("/customer-list")
    public String viewHomePage(Model model) {
        final List<Customer> customerList = customerService.getAllCustomers();
        model.addAttribute("customerList", customerList);
        return "customer-list";
    }

    @GetMapping("/new")
    public String showNewCustomerPage(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        return "new-customer";
    }

    @PostMapping("/save")
    public String saveCustomer(@Valid @ModelAttribute("customer") Customer customer, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "new-customer";
        }
        customerService.saveCustomer(customer);
        return "redirect:/customer-list";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditCustomerPage(@PathVariable(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("edit-customer");
        Customer customer = customerService.getCustomer(id);
        if (customer == null) {  // If some error happens while trying to get the customer, return home.
            return new ModelAndView("redirect:/");
        }
        mav.addObject("customer", customer);
        return mav;
    }

    @PostMapping("/update/{id}")
    public String updateCustomer(@PathVariable(name = "id") Long id, @ModelAttribute("customer") Customer customer, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/edit/{id}";
        }
        if (!id.equals(customer.getId())) {  // I'm not sure how this would ever get triggered. The user would have to edit the post request themselves.
            model.addAttribute("message",
                    "Cannot update, customer id " + customer.getId()
                            + " doesn't match id to be updated: " + id + ".");
            return "error-page";
        }
        customerService.saveCustomer(customer);
        return "redirect:customer-list";
    }

    @RequestMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable(name = "id") Long id) {
        customerService.deleteCustomer(id);
            return "redirect:/customer-list";
    }
}

package cnlabs.CustomerWebsite.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AppController {

    @GetMapping({"/", "/index"})
    public String index() {
        return "index";
    }

    @GetMapping("/fragments")
    public String getFragments() {
        return "fragments.html";
    }

    @GetMapping("/customer-view")
    public String showCustomerView() {
        return "customer-view";
    }
}

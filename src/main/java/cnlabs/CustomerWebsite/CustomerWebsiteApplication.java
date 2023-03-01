package cnlabs.CustomerWebsite;

import cnlabs.CustomerWebsite.Models.Book;
import cnlabs.CustomerWebsite.Models.Customer;
import cnlabs.CustomerWebsite.Models.Role;
import cnlabs.CustomerWebsite.Models.User;
import cnlabs.CustomerWebsite.Services.BookService;
import cnlabs.CustomerWebsite.Services.CustomerService;
import cnlabs.CustomerWebsite.Services.RoleService;
import cnlabs.CustomerWebsite.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.Arrays;

@SpringBootApplication
public class CustomerWebsiteApplication implements CommandLineRunner {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(CustomerWebsiteApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleService.RoleRepositoryIsEmpty()) {
            Role roleUser = roleService.saveRole(new Role(Role.Roles.ROLE_USER));
            Role roleAdmin = roleService.saveRole(new Role(Role.Roles.ROLE_ADMIN));

            User user = User.builder().email("user@user.com").username("user").password("password").build();
            User userAdmin = User.builder().email("admin@user.com").username("admin").password("admin").build();
            userService.registerUser(user);
            userService.registerAdmin(userAdmin);
        }

        if (bookService.getAllBooks().isEmpty()) {
            Book book = Book.builder().title("Book1").author("Author1").build();
            Book book2 = Book.builder().title("Book2").author("Author2").build();
            bookService.saveAllBook(Arrays.asList(book, book2));
        }

        if (customerService.getAllCustomers().isEmpty()) {
            Book book = bookService.getAllBooks().get(0);
            customerService.saveAllCustomer(Arrays.asList(
                            Customer.builder().fullName("Customer 1").emailAddress("customer1@gmail.com").address("Customer Address One").age(30).book(book).bookCheckoutDate(LocalDate.now().minusDays(20)).build(),
                            Customer.builder().fullName("Customer 2").emailAddress("customer2@gmail.com").address("Customer Address Two").age(28).build(),
                            Customer.builder().fullName("Customer 3").emailAddress("customer3@gmail.com").address("Customer Address Three").age(32).build()
                    )
            );
        }
    }
}

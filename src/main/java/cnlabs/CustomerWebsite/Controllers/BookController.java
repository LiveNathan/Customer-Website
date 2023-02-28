package cnlabs.CustomerWebsite.Controllers;

import cnlabs.CustomerWebsite.Models.Book;
import cnlabs.CustomerWebsite.Models.Customer;
import cnlabs.CustomerWebsite.Services.BookService;
import cnlabs.CustomerWebsite.Services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/book")
public class BookController {

    @Autowired
    private final BookService bookService;

    @Autowired
    private final CustomerService customerService;

    @GetMapping
    public String viewBooks(Model model) {
        final List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "/book";
    }

    @GetMapping("/new")
    public String showNewBookPage(Model model) {
        Book book = new Book();
        model.addAttribute("book", book);
        return "new-book";
    }

    @PostMapping("/save")
    public String saveBook(@Valid @ModelAttribute("book") Book book, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "new-book";
        }
        bookService.saveBook(book);
        return "redirect:/book";
    }

    @GetMapping("/edit/{id}")
    public String showEditBookPage(@PathVariable Long id, Model model) {
        Book book = bookService.getBook(id);
        model.addAttribute("book", book);
        return "edit-book";
    }

    @PostMapping("/update/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute("book") Book book, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/book/edit/{id}";
        }
        if (!id.equals(book.getId())) {
            model.addAttribute("message",
                    "Cannot update, book id " + book.getId()
                            + " doesn't match id to be updated: " + id + ".");
            return "error-page";
        }
        bookService.saveBook(book);
        return "redirect:/book";
    }

    @GetMapping("/assign/{id}")
    public String showAssignBookPage(@PathVariable Long id, Model model) {
        Customer customer = customerService.getCustomer(id);
        List<Book> books = bookService.getAvailableBooks();
        model.addAttribute("customer", customer);
        model.addAttribute("books", books);
        return "assign-book";
    }

    @PostMapping("/assign")
    public String assignBook(@RequestParam Long customerId, @RequestParam Long bookId) {
        Customer customer = customerService.getCustomer(customerId);
        customer.setBook(bookService.getBook(bookId));
        customer.setBookCheckoutDate(new Date());
        customerService.saveCustomer(customer);
        return "redirect:/customer-list";
    }

    @GetMapping("/remove/{id}")
    public String removeBook(@PathVariable Long id) {
        Customer customer = customerService.getCustomer(id);
        customer.setBook(null);
        customerService.saveCustomer(customer);
        return "redirect:/customer-list";
    }

    @RequestMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/book";
    }
}
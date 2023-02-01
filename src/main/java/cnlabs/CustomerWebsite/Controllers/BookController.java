package cnlabs.CustomerWebsite.Controllers;

import cnlabs.CustomerWebsite.Models.Book;
import cnlabs.CustomerWebsite.Services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {
    @Autowired
    private final BookService bookService;

    @GetMapping("/book")
    public String viewBooks(Model model) {
        final List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "/book";
    }

    @GetMapping("/book/new")
    public String showNewBookPage(Model model) {
        Book book = new Book();
        model.addAttribute("book", book);
        return "new-book";
    }

    @PostMapping("/book/save")
    public String saveBook(@Valid @ModelAttribute("book") Book book, BindingResult result, Model model){
        if (result.hasErrors()) {
            return "new-book";
        }
        bookService.saveBook(book);
        return "redirect:/book";
    }

    @GetMapping("/book/edit/{id}")
    public String showEditBookPage(@PathVariable Long id, Model model) {
        Book book = bookService.getBook(id);
        model.addAttribute("book", book);
        return "edit-book";
    }

    @PostMapping("/book/update/{id}")
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

    @RequestMapping("book/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/book";
    }
}
package cnlabs.CustomerWebsite.Services;

import cnlabs.CustomerWebsite.Models.Book;
import cnlabs.CustomerWebsite.Repos.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    @Autowired
    final BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getAvailableBooks() {
        return bookRepository.findAll().stream().filter(book -> book.getCustomer() == null).collect(Collectors.toList());
    }

    @Transactional
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public Book getBook(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public List<Book> saveAllBook(List<Book> bookList) {
        return bookRepository.saveAll(bookList);
    }
}

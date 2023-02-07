package cnlabs.CustomerWebsite.Models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "books")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Title must not be blank.")
    private String title;

    @NotBlank(message = "Author must not be blank.")
    private String author;

    @OneToOne(mappedBy = "book")
    private Customer customer;

    @Override
    public String toString() {
        return title + " by " + author;
    }
}
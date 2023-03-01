package cnlabs.CustomerWebsite.Models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "customers")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank(message = "Full name is required.")
    private String fullName;

    @Email
    @NotBlank(message = "Email must not be blank.")
    private String emailAddress;

    @Max(message = "Age must be less than 130.", value = 130)
    @Min(message = "Age must be higher than zero.", value = 0)
    private Integer age;

    @NotBlank(message = "Address must not be blank.")
    private String address;

    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "book_id")
    private Book book;

//    @Temporal(TemporalType.DATE)
//    @Column(name = "book_checkout_date")
//    private Date bookCheckoutDate;

    @Column(name = "book_checkout_date")
    private LocalDate bookCheckoutDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Customer customer = (Customer) o;
        return id != null && Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return fullName + " (" + emailAddress + ")";
    }
}
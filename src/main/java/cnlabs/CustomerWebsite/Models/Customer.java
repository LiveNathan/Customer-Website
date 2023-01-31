package cnlabs.CustomerWebsite.Models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
@Table(name = "customers")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
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

    private String address;

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
}
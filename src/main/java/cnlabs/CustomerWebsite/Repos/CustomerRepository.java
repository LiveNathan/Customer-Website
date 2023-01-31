package cnlabs.CustomerWebsite.Repos;

import cnlabs.CustomerWebsite.Models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
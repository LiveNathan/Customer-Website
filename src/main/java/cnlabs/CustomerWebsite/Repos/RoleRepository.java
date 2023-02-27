package cnlabs.CustomerWebsite.Repos;

import cnlabs.CustomerWebsite.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(Role.Roles role);
}
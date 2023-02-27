package cnlabs.CustomerWebsite.Services;

import cnlabs.CustomerWebsite.Models.Role;
import cnlabs.CustomerWebsite.Repos.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public boolean RoleRepositoryIsEmpty() {
        return roleRepository.count() == 0;
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public Role findByRole(Role.Roles role) {
        Optional<Role> optionalRole = roleRepository.findByRole(role);
        if (optionalRole.isEmpty()) {
//            throw new RuntimeException("Role not found!");
            return null;  // I'm thinking this will allow the application to continue even if one of the roles is not found.
        }
        return optionalRole.get();
    }
}

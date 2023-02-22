package cnlabs.CustomerWebsite.Services;

import cnlabs.CustomerWebsite.Models.Role;
import cnlabs.CustomerWebsite.Models.User;
import cnlabs.CustomerWebsite.Repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private RoleService roleService;

    public void registerUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));

        // Get USER role from DB and add it to the user
        Role userRole = roleService.findByRole(Role.Roles.ROLE_USER);
        user.setRole(userRole);
//        List<Role> rolesUser = Collections.singletonList(userRole);
//        user.getAuthorities().addAll(rolesUser);

        userRepository.save(user);
    }

    public void registerAdmin(User user) {
        user.setPassword(encoder.encode(user.getPassword()));

        // Get USER role from DB and add it to the user
//        Role userRole = roleService.findByRole(Role.Roles.ROLE_USER);
        Role userAdmin = roleService.findByRole(Role.Roles.ROLE_ADMIN);
        user.setRole(userAdmin);
//        List<Role> rolesUser = Arrays.asList(userRole, userAdmin);
//        user.getAuthorities().addAll(rolesUser);

        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsernameIgnoreCase(username);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException(username + " is not a valid username! Check for typos and try again.");
        }

        return optionalUser.get();
    }

    public boolean UserRepositoryIsEmpty() {
        return userRepository.count() == 0;
    }
}

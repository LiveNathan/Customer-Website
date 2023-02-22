package cnlabs.CustomerWebsite.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Long id;

    @NotBlank(message = "Username must not be blank.")
    @Column(nullable = false, unique = true)
    public String username;

    @NotBlank(message = "Password must not be blank.")
    @Column(nullable = false)
    //make sure that the password is never sent out but can be read when creating a new user
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Email(message = "email format: name@domain.com")
    @NotBlank(message = "Password must not be blank!")
    @Column(nullable = false, unique = true)
    private String email;

    @Builder.Default  // Default value for the builder.
    @Column(nullable = false)
    private boolean isAccountNonExpired = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean isAccountNonLocked = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean isCredentialsNonExpired = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean isEnabled = true;

    public User(String username, String password, String email, String name) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @OneToOne(optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }
}
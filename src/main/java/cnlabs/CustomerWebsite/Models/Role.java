package cnlabs.CustomerWebsite.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    // The most common option to map an enum value to and from its database representation in JPA before 2.1 is to use the @Enumerated annotation.
    // With @Enumerated(EnumType.STRING), we can safely add new enum values or change our enum's order. However, renaming an enum value will still break the database data.
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Roles role = Roles.ROLE_USER;

    public Role(Roles role) {
        this.role = role;
    }

    @JsonIgnore
    public String getAuthority() {
        return role.toString();
    }

    public enum Roles {
        ROLE_USER,
        ROLE_ADMIN
    }

}
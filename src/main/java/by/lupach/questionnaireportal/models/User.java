package by.lupach.questionnaireportal.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
@Schema(description = "User entity represents an application user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the user", example = "42", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "User's email address used as login", example = "user@example.com", required = true)
    private String email;

    @Column(nullable = false)
    @Schema(description = "User's password (hashed)", required = true, accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    @Column(name = "first_name", nullable = false)
    @Schema(description = "User's first name", example = "John", required = true)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @Schema(description = "User's last name", example = "Doe", required = true)
    private String lastName;

    @Column(name = "phone_number")
    @Schema(description = "User's phone number", example = "+1234567890", required = false)
    private String phoneNumber;

    @OneToMany(mappedBy = "author")
    @Schema(description = "List of questionnaires created by the user")
    private List<Questionnaire> questionnaires;

    @Schema(description = "Indicates whether the user account is enabled", example = "true")
    private boolean enabled = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
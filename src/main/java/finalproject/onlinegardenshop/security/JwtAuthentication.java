package finalproject.onlinegardenshop.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Custom implementation of the {@link Authentication} interface to represent JWT Authentication.
 * <p>
 * This class holds the authentication information of a user, which includes the username,
 * roles, and other user-specific data. It is used within the Spring Security framework to
 * represent the principal being authenticated.
 * </p>
 *
 * @author A-R
 * @version 1.0
 * @Getter - Lombok annotation to generate getters for all fields.
 * @Setter - Lombok annotation to generate setters for all fields.
 * @since 1.0
 */
@Getter
@Setter
public class JwtAuthentication implements Authentication {

    /**
     * Indicates whether the user is authenticated.
     */
    private boolean authenticated;

    /**
     * The login of the authenticated user.
     */
    private String login;

    /**
     * The first name of the authenticated user.
     */
    private String firstName;

    /**
     * The roles granted to the authenticated user.
     */
    private Set<SimpleGrantedAuthority> roles;

    /**
     * Constructor to initialize a JwtAuthentication instance with a username and a collection of roles.
     *
     * @param login the username of the authenticated user.
     * @param roles    a collection of roles granted to the user.
     */
    public JwtAuthentication(String login, Collection<String> roles) {
        this.login = login;
        this.roles = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return login;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return firstName;
    }
}

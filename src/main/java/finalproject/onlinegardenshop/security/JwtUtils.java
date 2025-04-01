package finalproject.onlinegardenshop.security;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for handling JWT-related operations.
 * <p>
 * This utility class provides a method for generating a {@link JwtAuthentication} object from JWT claims.
 * </p>
 *
 * @Component - Indicates that an annotated class is a component. Such classes are considered as candidates
 *              for auto-detection when using annotation-based configuration and classpath scanning.
 *
 * @author A-R
 * @version 1.0
 * @since 1.0
 */
@Component
public class JwtUtils {

    /**
     * Generates a {@link JwtAuthentication} object from JWT claims.
     * <p>
     * This method extracts the username and roles from the provided JWT claims,
     * and creates a {@link JwtAuthentication} object with this information.
     * </p>
     *
     * @param claims the JWT claims.
     * @return a JwtAuthentication object containing the username and roles.
     */
    public static JwtAuthentication generate(Claims claims) {
        // Extract the username from the claims
        String username = claims.getSubject();
        // Extract the roles list from the claims
        List<?> rolesObjectList = claims.get("roles", List.class);
        // Convert the roles list to a list of strings
        List<String> roles = rolesObjectList.stream()
                .map(Object::toString)
                .collect(Collectors.toList());
        // Create and return a JwtAuthentication object with the extracted information
        return new JwtAuthentication(username, roles);
    }
}



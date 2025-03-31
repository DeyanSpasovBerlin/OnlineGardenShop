package finalproject.onlinegardenshop.security;

//import de.telran.bankapp.dto.JwtRequest;
//import de.telran.bankapp.dto.JwtResponse;
//import de.telran.bankapp.entity.AppUser;
//import de.telran.bankapp.service.AppUserService;
import finalproject.onlinegardenshop.dto.JwtRequest;
import finalproject.onlinegardenshop.dto.JwtResponse;
import finalproject.onlinegardenshop.entity.Users;
import finalproject.onlinegardenshop.service.UsersService;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for handling authentication-related operations.
 * <p>
 * This service provides methods for user login, access token generation, refresh token generation,
 * and obtaining authentication information from the security context.
 * </p>
 *
 * @Service               - Indicates that an annotated class is a service component.
 * @RequiredArgsConstructor - Lombok annotation to generate a constructor for all final fields,
 *                           with parameter order same as field order.
 *
 * @author A-R
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    /**
     * The user service for fetching user data.
     */
    private final UsersService userService;

    /**
     * The JWT provider for generating and validating JWT tokens.
     */
    private final JwtProvider jwtProvider;

    private final PasswordEncoder encoder;

    /**
     * Handles user login and returns JWT tokens upon successful authentication.
     *
     * @param authRequest the authentication request containing user credentials.
     * @return a JwtResponse containing the generated access and refresh tokens.
     * @throws AuthException if the user is not found or the password is incorrect.
     */
    public JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException {
        final Users user = userService.getByLogin(authRequest.getLogin())
                .orElseThrow(() -> new AuthException("User is not found"));
        if (encoder.matches(authRequest.getPassword(), user.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            user.setRefreshToken(refreshToken);
            userService.save(user);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new AuthException("Wrong password");
        }
    }

    /**
     * Generates a new access token using a valid refresh token.
     * <p>
     * This method first validates the provided refresh token using the {@link JwtProvider}.
     * If the token is valid, it extracts the user login from the token claims,
     * retrieves the stored refresh token for the user, and compares it with the provided token.
     * If they match, it fetches the user data, generates a new access token,
     * and returns a {@link JwtResponse} with the new access token.
     * If any of the validation steps fail, it returns a {@link JwtResponse} with null values.
     * </p>
     *
     * @param refreshToken the refresh token.
     * @return a JwtResponse containing the generated access token, or null values if validation fails.
     * @throws AuthException if the user is not found.
     */
    public JwtResponse getAccessToken(@NonNull String refreshToken) throws AuthException {
        // Validate the provided refresh token
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            // Extract claims from the refresh token
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            // Get the user login from the token claims
            final String login = claims.getSubject();
            // Fetch the user data
            final Users user = userService.getByLogin(login)
                    .orElseThrow(() -> new AuthException("User is not found"));
            // Retrieve the stored refresh token for the user
            final String savedRefreshToken = user.getRefreshToken();
            // Compare the stored refresh token with the provided token
            if (savedRefreshToken != null && savedRefreshToken.equals(refreshToken)) {
                // Generate a new access token
                final String accessToken = jwtProvider.generateAccessToken(user);
                // Return a JwtResponse with the new access token
                return new JwtResponse(accessToken, null);
            }
        }
        throw new AuthException("Token is not valid");
    }


    /**
     * Refreshes both access and refresh tokens using a valid refresh token.
     * <p>
     * This method first validates the provided refresh token using the {@link JwtProvider}.
     * If the token is valid, it extracts the user login from the token claims,
     * retrieves the stored refresh token for the user, and compares it with the provided token.
     * If they match, it fetches the user data, generates new access and refresh tokens,
     * updates the stored refresh token for the user, and returns a {@link JwtResponse} with the new tokens.
     * If any of the validation steps fail, it throws an {@link AuthException} with a message indicating
     * an invalid JWT token.
     * </p>
     *
     * @param refreshToken the refresh token.
     * @return a JwtResponse containing the generated access and refresh tokens.
     * @throws AuthException if the refresh token is invalid or the user is not found.
     */
    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException {
        // Validate the provided refresh token
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            // Extract claims from the refresh token
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            // Get the user login from the token claims
            final String login = claims.getSubject();
            // Fetch the user data
            final Users user = userService.getByLogin(login)
                    .orElseThrow(() -> new AuthException("User is not found"));
            // Retrieve the stored refresh token for the user
            final String savedRefreshToken = user.getRefreshToken();
            // Compare the stored refresh token with the provided token
            if (savedRefreshToken != null && savedRefreshToken.equals(refreshToken)) {
                // Generate new access and refresh tokens
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                // Update the stored refresh token for the user
                user.setRefreshToken(newRefreshToken);
                userService.save(user);
                // Return a JwtResponse with the new access and refresh tokens
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        // Throw an AuthException if validation fails
        throw new AuthException("Invalid JWT token");
    }


    /**
     * Retrieves the authentication information from the security context.
     *
     * @return the JwtAuthentication object containing the authentication information.
     */
    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}

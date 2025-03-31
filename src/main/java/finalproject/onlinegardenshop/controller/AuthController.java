package finalproject.onlinegardenshop.controller;

//import de.telran.bankapp.dto.JwtRequest;
//import de.telran.bankapp.dto.JwtRequestRefresh;
//import de.telran.bankapp.dto.JwtResponse;
//import de.telran.bankapp.security.AuthService;
import finalproject.onlinegardenshop.dto.JwtRequest;
import finalproject.onlinegardenshop.dto.JwtRequestRefresh;
import finalproject.onlinegardenshop.dto.JwtResponse;
import finalproject.onlinegardenshop.security.AuthService;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for handling authentication and token operations.
 * <p>
 * This controller provides endpoints for user login, obtaining a new access token,
 * and refreshing the token. It leverages the AuthService for these operations.
 * </p>
 *
 * @RestController      - Indicates that this class is a controller where
 *                        every method returns a domain object instead of a view.
 * @RequestMapping      - Maps HTTP requests to handler functions of MVC and REST controllers.
 * @RequiredArgsConstructor - Lombok annotation, generates a constructor for all final fields,
 *                           with parameter order same as field order.
 *
 * @author A-R
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    /**
     * The authentication service bean for handling authentication operations.
     */
    private final AuthService authService;

    /**
     * Handles user login and returns a JWT response.
     *
     * @param authRequest the authentication request containing username and password.
     * @return a ResponseEntity containing the JWT response.
     * @throws AuthException if authentication fails.
     */
    @PostMapping("login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) throws AuthException {
        final JwtResponse token = authService.login(authRequest);
        return ResponseEntity.ok(token);
    }

    /**
     * Obtains a new access token using a refresh token.
     *
     * @param request the request containing the refresh token.
     * @return a ResponseEntity containing the JWT response.
     * @throws AuthException if token refresh fails.
     */
    @PostMapping("token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody JwtRequestRefresh request) throws AuthException {
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    /**
     * Obtains a new refresh token using an existing refresh token.
     *
     * @param request the request containing the refresh token.
     * @return a ResponseEntity containing the JWT response.
     * @throws AuthException if token refresh fails.
     */
    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody JwtRequestRefresh request) throws AuthException {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

}


package finalproject.onlinegardenshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Data Transfer Object (DTO) for carrying JWT tokens in response to a successful authentication.
 * <p>
 * This class encapsulates the access and refresh tokens, along with the token type, which is
 * typically "Bearer". It is used to transfer JWT tokens to the client upon successful authentication.
 * </p>
 *
 * @Getter               - Lombok annotation to generate getters for all fields.
 * @AllArgsConstructor   - Lombok annotation to generate a constructor for all fields.
 *
 * @author A-R
 * @version 1.0
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public class JwtResponse {

    /**
     * The type of the token, typically "Bearer".
     */
    private final String type = "Bearer";

    /**
     * The access token for authenticating API requests.
     */
    private String accessToken;

    /**
     * The refresh token for obtaining a new access token.
     */
    private String refreshToken;

}


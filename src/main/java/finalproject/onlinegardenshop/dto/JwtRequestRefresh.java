package finalproject.onlinegardenshop.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for carrying the refresh token.
 * <p>
 * This class is used to transfer the refresh token from the client to the server
 * for operations such as obtaining a new access token or refreshing the refresh token itself.
 * </p>
 *
 * @Getter               - Lombok annotation to generate getters for all fields.
 * @Setter               - Lombok annotation to generate setters for all fields.
 *
 * @author A-R
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public class JwtRequestRefresh {

    /**
     * The refresh token used to obtain a new access token.
     */
    public String refreshToken;

}



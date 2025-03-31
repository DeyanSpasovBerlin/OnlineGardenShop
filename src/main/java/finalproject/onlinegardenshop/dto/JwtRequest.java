package finalproject.onlinegardenshop.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for carrying user login credentials.
 * <p>
 * This class is used to transfer data for user login operations, containing the necessary
 * credentials such as username (login) and password.
 * </p>
 *
 * @Setter               - Lombok annotation to generate setters for all fields.
 * @Getter               - Lombok annotation to generate getters for all fields.
 *
 * @author A-R
 * @version 1.0
 * @since 1.0
 */
@Setter
@Getter
public class JwtRequest {

    /**
     * The username (login) of the user. In my case E-mail
     */
    private String login;

    /**
     * The password of the user.
     */
    private String password;

}


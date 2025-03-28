package finalproject.onlinegardenshop.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import finalproject.onlinegardenshop.entity.enums.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsersDto {

    private Integer id;

    @NotNull(message="{validation.users.lastName}")
    @Pattern(regexp = "^[A-ZÜÄÖ][a-zA-Züäö]{0,44}$",message = "{validation.Users.lastName}")
    @Length(max = 45, message ="{validation.Users.lastName}")
    private String lastName;

    @NotNull(message = "{validation.users.firstName}")
    @Pattern(regexp = "^[A-ZÜÄÖ][a-zA-Züäö]{0,44}$",message = "{validation.Users.firstName}")
    private String firstName;

    @NotBlank(message = "{validation.users.emailNotNull}")//email must present
    @Email(regexp = "^[a-zA-Z][\\w.-]*@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "{validation.users.email}")
    private String email;

    @NotNull//phone must present
    @Pattern(
            regexp = "^\\+?[0-9 ]{7,15}$",
            message = "{validation.users.phone}"
    )
    private String phone;

    @NotNull(message = "{validation.users.password}")
    @Length(min = 8, message = "{validation.users.password}")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&\\-_])[A-Za-z\\d@$!%*?&\\-_]{8,}$",
            message = "{validation.users.password}"
    )
    private String password;


    @Enumerated(EnumType.STRING)
    private UserRole role;


}

package finalproject.onlinegardenshop.dto;

import finalproject.onlinegardenshop.entity.enums.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @NotNull(message="{validation.Users.lastName}")
    @Pattern(regexp = "^[A-ZÜÄÖ][a-zA-Züäö]{0,44}$",message = "{validation.Users.lastName}")
    @Length(max = 45, message ="{validation.Users.lastName}")
    private String lastName;

    @NotNull(message = "{validation.users.firstName}")
    @Pattern(regexp = "^[A-ZÜÄÖ][a-zA-Züäö]{0,44}$",message = "{validation.Users.firstName}")
    private String firstName;

    private String email;

    private String phone;

    private  String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;


}

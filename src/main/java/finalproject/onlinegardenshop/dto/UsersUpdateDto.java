package finalproject.onlinegardenshop.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = false)
public class UsersUpdateDto {//ето dto сделано только на те поля,
    // по которые разрешаем update Для того, что бы запретьит включат другие поля в Postman
    // добавляем JsonIgnoreProperties(ignoreUnknown = false)
    private Integer id;

    @NotNull(message = "{validation.users.lastName}")
    @Pattern(regexp = "^[A-ZÜÄÖ][a-zA-Züäö]{0,44}$", message = "{validation.users.lastName}")
    @Length(max = 45, message = "{validation.users.lastName}")
    private String lastName;

    @NotNull(message = "{validation.users.firstName}")
    @Pattern(regexp = "^[A-ZÜÄÖ][a-zA-Züäö]{0,44}$", message = "{validation.users.firstName}")
    private String firstName;

    @NotNull(message = "{validation.users.phone}")
    @Pattern(
            regexp = "^\\+?[0-9 ]{7,15}$",
            message = "{validation.users.phone}"
    )
    private String phone;

}

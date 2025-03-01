package finalproject.onlinegardenshop.dto;

import finalproject.onlinegardenshop.entity.enums.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsersDto {

    private Integer id;

    private String name;

    private String email;

    private String phone;

    private  String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

}

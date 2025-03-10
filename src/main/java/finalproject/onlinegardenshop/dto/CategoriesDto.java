package finalproject.onlinegardenshop.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoriesDto {

    private int id;

    @NotNull(message = "{validation.categories.name}")
    @Pattern(regexp = "^[A-ZÜÄÖ][a-zA-Züäö'\\- ]{0,254}$", message = "{validation.categories.name}")
    private String name;

}


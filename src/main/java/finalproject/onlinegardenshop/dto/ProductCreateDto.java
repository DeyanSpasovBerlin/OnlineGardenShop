package finalproject.onlinegardenshop.dto;

import jakarta.validation.constraints.Digits;
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
public class ProductCreateDto {

    @NotNull(message = "{validation.products.name}")
    @Pattern(regexp = "^[A-Za-zÜÄÖüäö'\\-., ]{1,255}$", message = "{validation.products.name}")
    private String name;

    private String category; // Приходит как String (название категории)

    private String description;

    @Digits(integer = 5, fraction = 2)
    private Double price;

    @Digits(integer = 5, fraction = 2)
    private Double discountPrice;

    private String imageUrl;

}


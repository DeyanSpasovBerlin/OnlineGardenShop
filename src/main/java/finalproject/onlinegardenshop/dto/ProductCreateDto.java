package finalproject.onlinegardenshop.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductCreateDto {

    @NotBlank(message = "{validation.products.name}")
    @Pattern(regexp = "^[A-Za-zÜÄÖüäö'\\-., ]{1,255}$", message = "{validation.products.name}")
    private String name;

    private String category;

    private String description;

    @Positive(message = "Price must be positive")
    @Digits(integer = 5, fraction = 2)
    private Double price;

    @Positive(message = "Price must be positive")
    @Digits(integer = 5, fraction = 2)
    private Double discountPrice;

    private String imageUrl;

}


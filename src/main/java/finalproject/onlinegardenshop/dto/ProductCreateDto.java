package finalproject.onlinegardenshop.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductCreateDto {

    @NotNull(message = "{validation.products.name}")
    @Pattern(regexp = "^[A-ZÜÄÖ][a-zA-Züäö ]{0,255}$", message = "{validation.products.name}")
    private String name;

    //@Pattern(regexp = "^[a-zA-Züäö]{1,255}$", message = "{validation.products.description}")
    private String description;

    @Digits(integer = 5, fraction = 2)
    private double price;

    @Digits(integer = 5, fraction = 2)
    private double discountPrice;

    //@Pattern(regexp = "^https?://[^\\s]+(\\.jpg|\\.jpeg|\\.png|\\.gif|\\.webp)$", message = "{validation.products.image_url}")
    private String imageUrl;

}

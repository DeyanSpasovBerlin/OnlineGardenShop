package finalproject.onlinegardenshop.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDto {

    private int id;

    @NotNull(message = "{validation.products.name}")
    @Pattern(regexp = "^[A-ZÜÄÖ][a-zA-Züäö]{1,100}$", message = "{validation.products.name}")
    private String name;

    //@Pattern(regexp = "^[a-zA-Züäö]{1,255}$", message = "{validation.products.description}")
    private String description;

    @Digits(integer = 5, fraction = 2)
    private double price;

    @Digits(integer = 5, fraction = 2)
    private double discountPrice;

    //@Pattern(regexp = "^https?://[^\\s]+(\\.jpg|\\.jpeg|\\.png|\\.gif|\\.webp)$", message = "{validation.products.image_url}")
    private String imageUrl;

    private Instant createdAt;

    private Instant updatedAt;

}

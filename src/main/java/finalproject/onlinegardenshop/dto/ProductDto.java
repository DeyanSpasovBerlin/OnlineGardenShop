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
    @Pattern(regexp = "^[A-ZÜÄÖ][a-zA-Züäö ]{0,255}$", message = "{validation.products.name}")
    private String name;

    private String description;

    @Digits(integer = 5, fraction = 2)
    private double price;

    @Digits(integer = 5, fraction = 2)
    private double discountPrice;

    private String imageUrl;

    private Instant createdAt;

    private Instant updatedAt;

}

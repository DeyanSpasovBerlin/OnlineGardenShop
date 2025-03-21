package finalproject.onlinegardenshop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
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
public class ProductsDto {

    private Integer id;

    @NotNull(message = "{validation.products.name}")
    @Pattern(regexp = "^[A-Za-zÜÄÖüäö'\\-., ]{1,255}$", message = "{validation.products.name}")
    private String name;

    private String category; // Название категории

    private String description;

    @Digits(integer = 5, fraction = 2)
    private Double price;

    @Digits(integer = 5, fraction = 2)
    private Double discountPrice;

    private String imageUrl;

    private Instant createdAt;

    private Instant updatedAt;

    // Геттеры для правильного отображения JSON
    @JsonProperty("price")
    public Double getPrice() {
        return price; // Всегда передаём установленное значение цены
    }

    @JsonInclude(JsonInclude.Include.NON_NULL) // Не включаем в JSON, если null
    @JsonProperty("discountPrice")
    public Double getDiscountPrice() {
        return discountPrice; // Если null, то не будет отображаться в JSON
    }
}


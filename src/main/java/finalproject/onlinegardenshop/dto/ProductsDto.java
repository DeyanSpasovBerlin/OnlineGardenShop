package finalproject.onlinegardenshop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({"id", "name", "category", "description", "imageUrl", "price", "discountPrice", "createdAt", "updatedAt"})
public class ProductsDto {

    private Integer id;

    @NotNull(message = "{validation.products.name}")
    @Pattern(regexp = "^[A-Za-zÜÄÖüäö'\\-., ]{1,255}$", message = "{validation.products.name}")
    private String name;

    private String category;

    private String description;

    @Digits(integer = 5, fraction = 2)
    private Double price;

    @Digits(integer = 5, fraction = 2)
    private Double discountPrice;

    private String imageUrl;

    private static final ZoneId LOCAL_ZONE = ZoneId.of("Europe/Berlin");

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(LOCAL_ZONE);

    // Эти поля нужны только для форматирования
    @JsonIgnore
    private transient Instant createdAtInstant;
    @JsonIgnore
    private transient Instant updatedAtInstant;

    @JsonProperty("createdAt")
    public String getCreatedAt() {
        return createdAtInstant != null ? FORMATTER.format(createdAtInstant) : null;
    }

    @JsonProperty("updatedAt")
    public String getUpdatedAt() {
        return updatedAtInstant != null ? FORMATTER.format(updatedAtInstant) : null;
    }

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


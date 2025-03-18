package finalproject.onlinegardenshop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoritesDto {
    private Integer id;

    @NotNull(message = "{validation.favorites.productId}")
    private Integer productsId;

    private String status;

}
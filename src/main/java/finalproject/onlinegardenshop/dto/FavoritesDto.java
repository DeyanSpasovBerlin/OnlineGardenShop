package finalproject.onlinegardenshop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoritesDto {
    private Integer id;

    @NotNull(message = "{validation.favorites.productId}")
    private Integer productsId;

    private String status;
    @Setter
    private Integer userId;
}
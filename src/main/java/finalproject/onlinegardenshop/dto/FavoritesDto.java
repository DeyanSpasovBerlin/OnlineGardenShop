package finalproject.onlinegardenshop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoritesDto {
    private Integer id;

    @Setter
    @Getter
    @NotNull(message = "{validation.favorites.productId}")
    private Integer productsId;

    private String status;
    @Setter
    private Integer userId;

}
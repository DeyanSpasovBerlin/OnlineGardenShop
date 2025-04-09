package finalproject.onlinegardenshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItemsDto {

    private Integer id;

    private Integer cartId;

    private Integer productsId;

    private Integer quantity;

    private String productsName;
    private Double productsPrice;

}

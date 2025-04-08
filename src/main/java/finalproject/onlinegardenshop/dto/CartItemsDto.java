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

    private String productsName;   // to show in addToCart product name and price
    private Double productsPrice;  // to show in addToCart product name and price

}

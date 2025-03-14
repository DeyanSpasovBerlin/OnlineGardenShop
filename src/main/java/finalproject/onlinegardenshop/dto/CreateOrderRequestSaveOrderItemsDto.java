package finalproject.onlinegardenshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequestSaveOrderItemsDto {

    private Integer productId;//
    private int quantity;
    private double priceAtPurchase;
}

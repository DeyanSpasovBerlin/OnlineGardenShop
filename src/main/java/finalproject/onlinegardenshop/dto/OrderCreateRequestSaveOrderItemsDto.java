package finalproject.onlinegardenshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateRequestSaveOrderItemsDto {

    private Integer productId;
    private String productName;
//    private String firstName;
//    private String lastName;
    private int quantity;
    private double priceAtPurchase;

}

package finalproject.onlinegardenshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemsDto {
    private Integer id;
    private Integer orderId;
    private Integer productId;
    private int quantity;
    private double priceAtPurchase;
}
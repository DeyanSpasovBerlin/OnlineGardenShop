package finalproject.onlinegardenshop.dto;

import finalproject.onlinegardenshop.entity.enums.DeliveryMethod;
import finalproject.onlinegardenshop.validation.ValidEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderCreateRequestDto {

    @NotNull(message = "{validation.CreateOrderRequest.items.notNull}")
    @Size(min = 1, message = "{validation.CreateOrderRequest.items.size}")
    private List<OrderItem> items;

    @NotNull(message = "{validation.CreateOrderRequest.deliveryAddress.notNull}")
    @Size(min = 5, max = 150, message = "{validation.CreateOrderRequest.deliveryAddress.size}")
    private String deliveryAddress;

    @NotNull(message = "{validation.CreateOrderRequest.deliveryMethod.notNull}")
    @ValidEnum(enumClass = DeliveryMethod.class, message = "{validation.CreateOrderRequest.deliveryMethod.invalid}")
    private String deliveryMethod;

    private Integer usersId;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class OrderItem {
        @NotNull(message = "{validation.CreateOrderRequest.orderItem.productId.notNull}")
        private String productId;

        @Min(value = 1, message = "{validation.CreateOrderRequest.orderItem.quantity.min}")
        private int quantity;
    }

}

package finalproject.onlinegardenshop.mapper;

import finalproject.onlinegardenshop.dto.OrderItemsDto;
import finalproject.onlinegardenshop.entity.OrderItem;

public class OrderItemMapper {
    public static OrderItemsDto toDto(OrderItem orderItem) {
        return new OrderItemsDto(
                orderItem.getId(),
                orderItem.getOrder().getId(),
                orderItem.getProduct().getId(),
                orderItem.getQuantity(),
                orderItem.getPriceAtPurchase()
        );
    }

    public static OrderItem toEntity(OrderItemsDto dto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(dto.getId());
        orderItem.setQuantity(dto.getQuantity());
        orderItem.setPriceAtPurchase(dto.getPriceAtPurchase());
        return orderItem;
    }
}

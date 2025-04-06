package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.OrderItemsDto;
import finalproject.onlinegardenshop.service.OrderItemsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders/{orderId}/order-items")
@Tag(name = "Order Items Controller", description = "REST API to manage order items in the app")
public class OrderItemsController {
    private final OrderItemsService service;

    public OrderItemsController(OrderItemsService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Returns a list of order items for a specific order if authorized")
    public ResponseEntity<List<OrderItemsDto>> getOrderItems(@PathVariable Integer orderId) {
        // Получаем список позиций товаров для заказа, если пользователь имеет доступ к этому заказу
        List<OrderItemsDto> orderItems = service.getOrderItemsForOrder(orderId);
        return ResponseEntity.ok(orderItems);
    }

    @GetMapping("/{orderItemId}")
    @Operation(summary = "Returns a specific order item if authorized")
    public ResponseEntity<OrderItemsDto> getOrderItemById(@PathVariable Integer orderItemId) {
        // Получаем конкретную позицию товара по ID
        OrderItemsDto orderItemDto = service.getOrderItemById(orderItemId);
        return ResponseEntity.ok(orderItemDto);
    }
}

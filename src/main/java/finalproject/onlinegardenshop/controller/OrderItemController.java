package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.OrderItemsDto;
import finalproject.onlinegardenshop.service.OrderItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/orders/{orderId}/order-items")
public class OrderItemController {
    private final OrderItemService service;

    public OrderItemController(OrderItemService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderItemsDto>> getAllOrderItems(@PathVariable Integer orderId) {
        return ResponseEntity.ok(service.getAllOrderItems());
    }
}


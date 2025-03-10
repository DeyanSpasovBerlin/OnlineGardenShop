package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.OrderItemsDto;
import finalproject.onlinegardenshop.service.OrderItemsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/orders/{orderId}/order-items")
public class OrderItemsController {
    private final OrderItemsService service;
    private Integer orderId;

    public OrderItemsController(OrderItemsService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderItemsDto>> getAllOrderItems(@PathVariable Integer orderId) {
        this.orderId = orderId;
        return ResponseEntity.ok(service.getAllOrderItems());
    }
}


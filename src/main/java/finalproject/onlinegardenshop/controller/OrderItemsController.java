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
    private Integer orderId;

    public OrderItemsController(OrderItemsService service) {
        this.service = service;
    }

    @GetMapping("/all")
    @Operation(summary = "Returns a list of all order items available")
    public ResponseEntity<List<OrderItemsDto>> getAllOrderItems(@PathVariable Integer orderId) {
        this.orderId = orderId;
        return ResponseEntity.ok(service.getAllOrderItems());
    }
}


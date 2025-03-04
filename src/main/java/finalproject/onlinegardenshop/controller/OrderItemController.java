package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.OrderItemsDto;
import finalproject.onlinegardenshop.service.OrderItemService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/order-items")
public class OrderItemController {
    private final OrderItemService service;

    public OrderItemController(OrderItemService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<OrderItemsDto> getAllOrderItems() {
        return service.getAllOrderItems();
    }

    @GetMapping("/{id}")
    public OrderItemsDto getOrderItemById(@PathVariable Integer id) {
        return service.getOrderItemById(id);
    }

    @PostMapping
    public OrderItemsDto saveOrderItem(@RequestBody OrderItemsDto dto) {
        return service.saveOrderItem(dto);
    }
}


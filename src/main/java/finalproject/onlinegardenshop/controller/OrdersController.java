package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.CartItemsDto;
import finalproject.onlinegardenshop.dto.OrdersDto;
import finalproject.onlinegardenshop.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
@Validated
public class OrdersController {

    private final OrdersService ordersService;

    @Autowired
    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping("/all")
    public List<OrdersDto> getAllOrders(){
        return ordersService.getAll();
    }

    @GetMapping("{id}")
    public Optional<OrdersDto> getOrdersById(@PathVariable Integer id) {
        return Optional.ofNullable(ordersService.getOrderssById(id));
    }

}

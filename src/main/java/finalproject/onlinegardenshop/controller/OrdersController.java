package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.CreateOrderRequestDto;
import finalproject.onlinegardenshop.dto.OrdersDto;
import finalproject.onlinegardenshop.entity.Orders;
import finalproject.onlinegardenshop.service.OrdersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    // REST API from tex docs:
    //    1 •	•	Оформление заказа  ->   controller
    @PostMapping
    public ResponseEntity<OrdersDto> createOrder(@Valid @RequestBody CreateOrderRequestDto request) {
        OrdersDto createdOrder = ordersService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestHeader("userId") Integer userId,
                                           @Valid @RequestBody OrdersDto request) {
        ordersService.checkout(userId,
                request.getDeliveryAddress()
                , request.getContactPhone(),
                String.valueOf(request.getDeliveryMethod()));
        return ResponseEntity.ok("Order placed successfully!");
    }
    /*
    POST: URL: http://localhost:8080/orders/checkout
    Headers: userId: 1
    BODY: {empty}
     */
    //•	История покупок пользователя
    //o	URL: /orders/history
    //o	Метод: GET
    @GetMapping("/history")
    public List<OrdersDto> getOrdersByUser(@RequestHeader("userId") Integer userId) {
        return ordersService.getOrdersByUser(userId);
    }
    /*
    GET  http://localhost:8080/orders/history
    HEADERS  userId 1
     */

    @PatchMapping("/canceled")
    public ResponseEntity<Void> cancelOrder(@RequestParam Integer orderId){
        ordersService.cancelOrdersStatus(orderId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    /*
    PATCH  http://localhost:8080/orders/canceled?orderId=2
     */

    // Get orders for a specific deleted user
    @GetMapping("/deleted/{userId}")
    public List<Orders> getOrdersByDeletedUser(@PathVariable Integer userId) {
        return ordersService.getOrdersByDeletedUser(userId);
    }
    /*
    GET http://localhost:8080/orders/deleted/15
     */

    // Get all orders from deleted users
    @GetMapping("/deleted")
    public List<Orders> getAllDeletedUsersOrders() {
        return ordersService.getAllDeletedUsersOrders();
    }
    /*
    GET http://localhost:8080/orders/deleted
     */

}

package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.CreateOrderRequestDto;
import finalproject.onlinegardenshop.dto.OrdersDto;
import finalproject.onlinegardenshop.entity.Orders;
import finalproject.onlinegardenshop.service.OrdersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
@Validated
@Tag(name = "Orders Controller", description = "REST API to manage order-related operations in the app")
public class OrdersController {

    private final OrdersService ordersService;

    @Autowired
    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping("/all")
    @Operation(summary = "Returns a list of all orders")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<OrdersDto> getAllOrders(){
        return ordersService.getAll();
    }

    @GetMapping("{id}")
    @Operation(summary = "Returns an order by its id")
    public Optional<OrdersDto> getOrdersById(@PathVariable Integer id) {
        return Optional.ofNullable(ordersService.getOrderssById(id));
    }

    // REST API from tex docs:
    //    1 •	•	Оформление заказа  ->   controller
    @PostMapping
    @Operation(summary = "Creates an order")
    public ResponseEntity<OrdersDto> createOrder(@Valid @RequestBody CreateOrderRequestDto request) {
        OrdersDto createdOrder = ordersService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @PostMapping("/checkout")
    @Operation(summary = "Order checkout API")
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
    BODY: {
    "deliveryAddress": "Berlin6, Germany",
        "contactPhone": "+493456784564789",
        "deliveryMethod": "COURIER_DELIVERY"
}
     */
    //•	История покупок пользователя
    //o	URL: /orders/history
    //o	Метод: GET
    @GetMapping("/history")
    @Operation(summary = "Returns history of orders placed by a user")
    public List<OrdersDto> getOrdersByUser(@RequestHeader("userId") Integer userId) {
        return ordersService.getOrdersByUser(userId);
    }
    /*
    GET  http://localhost:8080/orders/history
    HEADERS  userId 1
     */

    @PatchMapping("/canceled")
    @Operation(summary = "Cancels an order by its id")
    public ResponseEntity<Void> cancelOrder(@RequestParam Integer orderId){
        ordersService.cancelOrdersStatus(orderId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    /*
    PATCH  http://localhost:8080/orders/canceled?orderId=2
     */

    // Get orders for a specific deleted user
    @GetMapping("/deleted/{userId}")
    @Operation(summary = "Returns a list of orders of a specific deleted user by user id")
    public List<OrdersDto> getOrdersByDeletedUser(@PathVariable Integer userId) {
        return ordersService.getOrdersByDeletedUser(userId);
    }
    /*
    GET http://localhost:8080/orders/deleted/15
     */

    // Get all orders from deleted users
    @GetMapping("/deleted")
    @Operation(summary = "Returns a list of all orders placed by a deleted user")
    public List<OrdersDto> getAllDeletedUsersOrders() {
        return ordersService.getAllDeletedUsersOrders();
    }
    /*
    GET http://localhost:8080/orders/deleted
     */

}

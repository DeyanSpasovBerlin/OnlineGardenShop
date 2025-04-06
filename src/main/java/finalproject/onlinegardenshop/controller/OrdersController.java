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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Optional<OrdersDto> getOrdersById(@PathVariable Integer id) {
        return Optional.ofNullable(ordersService.getOrderssById(id));
    }

    @GetMapping()
    @Operation(summary = "Returns last order from autorised user")
    public Optional<OrdersDto> getLastOrdersByUser() {
        return Optional.ofNullable(ordersService.getLastOrdersByUser());
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
    public ResponseEntity<String> checkout(@Valid @RequestBody OrdersDto request) {
        ordersService.checkout(request.getDeliveryAddress(),
                               request.getContactPhone(),
                               String.valueOf(request.getDeliveryMethod()));
        return ResponseEntity.ok("Order placed successfully!");
    }
        /*
    POST: URL: http://localhost:8080/orders/checkout
    BODY: {
            "deliveryAddress": "Berlin6, Germany",
             "contactPhone": "+49 101 345678456",
              "deliveryMethod": "COURIER_DELIVERY"
          }
     */

    //•	История покупок пользователя
    //o	URL: /orders/history
    //o	Метод: GET
    @GetMapping("/history")
    @Operation(summary = "Returns history of orders placed by a user")
    public List<OrdersDto> getOrdersByUser() {
        return ordersService.getOrdersByUser();
    }
    /*
    GET  http://localhost:8080/orders/history
     */

    @GetMapping("/historyByAdmin")
    public List<OrdersDto> getOrdersByUserFromAdmin(@RequestHeader("userId") Integer userId) {
        return ordersService.getOrdersByUserFromAdmin(userId);
    }
    /*
    GET  http://localhost:8080/orders/history
    HEADERS  userId 1
     */

    @PatchMapping("/canceledByAdmin")
    @Operation(summary = "Cancels an order by its id from ADMIN")
    public ResponseEntity<Void> cancelOrderAdmin(@RequestParam Integer orderId){
        ordersService.cancelOrdersStatusByAdmin(orderId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    /*
    PATCH  http://localhost:8080/orders/canceled?orderId=2
     */

    @PatchMapping("/canceledByUser")
    @Operation(summary = "Cancels the last order by authorized user")
    public ResponseEntity<Void> cancelOrderAdmin(){
        ordersService.cancelOrdersStatusByUser();
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
        /*
    PATCH  http://localhost:8080/orders/canceled
     */


    // Get orders for a specific deleted user
    @GetMapping("/deleted/{userId}")
    @Operation(summary = "Returns a list of orders of a specific deleted user by user id")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<OrdersDto> getOrdersByDeletedUser(@PathVariable Integer userId) {
        return ordersService.getOrdersByDeletedUser(userId);
    }
    /*
    GET http://localhost:8080/orders/deleted/15
     */

    // Get all orders from deleted users
    @GetMapping("/deleted")
    @Operation(summary = "Returns a list of all orders placed by a deleted users")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<OrdersDto> getAllDeletedUsersOrders() {
        return ordersService.getAllDeletedUsersOrders();
    }
    /*
    GET http://localhost:8080/orders/deleted
     */

}

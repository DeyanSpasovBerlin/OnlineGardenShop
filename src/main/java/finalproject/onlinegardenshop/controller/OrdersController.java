package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.OrderCreateRequestDto;
import finalproject.onlinegardenshop.dto.OrdersDto;
import finalproject.onlinegardenshop.service.OrdersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @GetMapping("/sorted")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Operation(summary = "Returns filtered, paginated and sorted orders")
    public ResponseEntity<Page<OrdersDto>> getFilteredSortedOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "0") double minTotalPrice,
            @RequestParam(defaultValue = "100000") double maxTotalPrice
    ) {
        Page<OrdersDto> orders = ordersService.getFilteredAndSortedOrders(page, size, sortBy, minTotalPrice, maxTotalPrice);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @Operation(summary = "Returns an order by its id for use from ADMIN")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Optional<OrdersDto> getOrdersById(@PathVariable Integer id) {
        return Optional.ofNullable(ordersService.getOrderssById(id));
    }

    @GetMapping()
    @Operation(summary = "Returns last order from autorised user")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Optional<OrdersDto> getLastOrdersByUser() {
        return Optional.ofNullable(ordersService.getLastOrdersByUser());
    }

    @PostMapping
    @Operation(summary = "Creates an order for emergency use from ADMIN")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<OrdersDto> createOrder(@Valid @RequestBody OrderCreateRequestDto request) {
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

    @GetMapping("/history")
    @Operation(summary = "Returns history of orders placed by a user who is authorized")
    public List<OrdersDto> getOrdersByUser() {
        return ordersService.getOrdersByUser();
    }

    @GetMapping("/historyByAdmin")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Operation(summary = "Returns an hitory orders by userId for use from ADMIN")
    public List<OrdersDto> getOrdersByUserFromAdmin(@RequestHeader("userId") Integer userId) {
        return ordersService.getOrdersByUserFromAdmin(userId);
    }

    @PatchMapping("/canceledByAdmin")
    @Operation(summary = "Cancels an order by its id for emergency use from ADMIN")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Void> cancelOrderAdmin(@RequestParam Integer orderId){
        ordersService.cancelOrdersStatusByAdmin(orderId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PatchMapping("/canceledByUser")
    @Operation(summary = "Cancels the last order by authorized user")
    public ResponseEntity<Void> cancelOrderAdmin(){
        ordersService.cancelOrdersStatusByUser();
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/deleted/{userId}")
    @Operation(summary = "Returns a list of orders of a specific deleted user by user id for use from ADMIN")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<OrdersDto> getOrdersByDeletedUser(@PathVariable Integer userId) {
        return ordersService.getOrdersByDeletedUser(userId);
    }

    @GetMapping("/deleted")
    @Operation(summary = "Returns a list of all orders placed by a deleted users for use from ADMIN")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<OrdersDto> getAllDeletedUsersOrders() {
        return ordersService.getAllDeletedUsersOrders();
    }

}

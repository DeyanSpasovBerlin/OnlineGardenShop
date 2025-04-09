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

//    @GetMapping("/all")
//    @Operation(summary = "Returns a list of all orders")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
//    public List<OrdersDto> getAllOrders(){
//        return ordersService.getAll();
//    }
    //*******************************
//@GetMapping("/all")
//@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
//public Page<OrdersDto> getAllOrders(
//        @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
//        @RequestParam(required = false) String sortBy,
//        @RequestParam(required = false) String direction
//) {
//    Sort sort = pageable.getSort();
//
//    if (sortBy != null && direction != null) {
//        Sort.Direction dir = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
//        sort = Sort.by(dir, sortBy);
//        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
//    }
//
//    return ordersService.getAll(pageable);
//}
    //**************************************
//@GetMapping("/all")
//@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
//public Page<OrdersDto> getAllOrders(
//        @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
//        @RequestParam(required = false) String sortBy,
//        @RequestParam(required = false) String direction
//) {
//    // Проверка дали има параметри за сортиране
//    if (sortBy != null && direction != null) {
//        String[] sortByFields = sortBy.split(",");
//        String[] directions = direction.split(",");
//
//        // Проверка дали броят на полетата за сортиране съвпада с броя на посоките
//        if (sortByFields.length != directions.length) {
//            throw new IllegalArgumentException("Number of sortBy fields must match number of direction fields");
//        }
//
//        // Създаваме списък с Sort.Order
//        List<Sort.Order> orders = new ArrayList<>();
//        for (int i = 0; i < sortByFields.length; i++) {
//            Sort.Direction dir = directions[i].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
//            orders.add(Sort.Order.by(sortByFields[i]).with(dir));
//        }
//
//        // Създаваме нов Pageable обект с новото сортиране
//        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(orders));
//    }
//
//    // Получаваме всички поръчки през сървисния слой
//    return ordersService.getAll(pageable);
//}
        //**************************************
        @GetMapping("/all")
        @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
        @Operation(summary = "Returns a list of all orders for use from ADMIN")
        public Page<OrdersDto> getAllOrders(
                @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                @RequestParam(required = false) String sortBy,
                @RequestParam(required = false) String direction
        ) {
            return ordersService.getAllOrders(pageable, sortBy, direction);
        }
        //****************************

    //GET http://localhost:8080/orders/all?sortBy=createdAt&direction=desc
    //GET http://localhost:8080/orders/all?sortBy=createdAt&direction=desc&page=0&size=10
    //GET http://localhost:8080/orders/all?sortBy=createdAt,totalPrice&direction=desc,asc&page=0&size=10
    //GET http://localhost:8080/orders/all?sortBy=totalPrice,status&direction=asc,desc&page=0&size=10
    //GET http://localhost:8080/orders/all?sortBy=status,createdAt&direction=desc,asc&page=0&size=10
    //swagger:
    /*
       {
          "page": 0,
          "size": 2,
          "sort": ["createdAt", "totalPrice"],
          "sortBy": "createdAt",
          "direction": "desc"
        }
        {
          "page": 0,
          "size": 1,
          "sort": ["totalPrice", "createdAt"],
          "sortBy": "totalPrice",
          "direction": "desc"
        }
        {
          "page": 1,
          "size": 3,
          "sort": ["status", "totalPrice"],
          "sortBy": "status",
          "direction": "asc"
        }
     */
    //***************************************

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

    // REST API from tex docs:
    //    1 •	•	Оформление заказа  ->   controller
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
    @Operation(summary = "Returns history of orders placed by a user who is authorized")
    public List<OrdersDto> getOrdersByUser() {
        return ordersService.getOrdersByUser();
    }
    /*
    GET  http://localhost:8080/orders/history
     */

    @GetMapping("/historyByAdmin")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Operation(summary = "Returns an hitory orders by userId for use from ADMIN")
    public List<OrdersDto> getOrdersByUserFromAdmin(@RequestHeader("userId") Integer userId) {
        return ordersService.getOrdersByUserFromAdmin(userId);
    }
    /*
    GET  http://localhost:8080/orders/history
    HEADERS  userId 1
     */

    @PatchMapping("/canceledByAdmin")
    @Operation(summary = "Cancels an order by its id for emergency use from ADMIN")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
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
    @Operation(summary = "Returns a list of orders of a specific deleted user by user id for use from ADMIN")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<OrdersDto> getOrdersByDeletedUser(@PathVariable Integer userId) {
        return ordersService.getOrdersByDeletedUser(userId);
    }
    /*
    GET http://localhost:8080/orders/deleted/15
     */

    // Get all orders from deleted users
    @GetMapping("/deleted")
    @Operation(summary = "Returns a list of all orders placed by a deleted users for use from ADMIN")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<OrdersDto> getAllDeletedUsersOrders() {
        return ordersService.getAllDeletedUsersOrders();
    }
    /*
    GET http://localhost:8080/orders/deleted
     */

}

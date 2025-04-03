package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.AddToCartRequestDto;
import finalproject.onlinegardenshop.dto.CartDto;
import finalproject.onlinegardenshop.dto.CartFullDto;
import finalproject.onlinegardenshop.dto.CartItemsDto;
import finalproject.onlinegardenshop.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
@Validated
@Tag(name = "Cart Controller", description = "REST API to manage cart-related operations in the app")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/all")
    @Operation(summary = "Returns a list of all carts available in the app")
    public List<CartDto> gerAllCarts() {
        return cartService.getAll();
    }

    @GetMapping("/full")
    @Operation(summary = "Returns a list of full carts with cart items and users data")
    public List<CartFullDto> getFullCart() {
        return cartService.getAllCartWithItemsAndUsers();
    }

    @GetMapping("{id}")
    @Operation(summary = "Returns a cart by its id")
    public Optional<CartDto> getCartById(@PathVariable Integer id) {
        return Optional.ofNullable(cartService.getCartById(id));
    }

    @PostMapping
    @Operation(summary = "Adds product(s) to a user's cart")
    public ResponseEntity<String> addToCart(@RequestHeader("userId") Integer userId,//header is most security
                                            @Valid @RequestBody AddToCartRequestDto request) {
        cartService.addToCart(userId, request);
        return ResponseEntity.ok("Product added to cart successfully");
    }
//    public ResponseEntity<String> addToCart(@RequestParam("userId") Integer userId,//url is faster and ligter
//                                            @Valid @RequestBody AddToCartRequestDto request) {
//        cartService.addToCart(userId, request);
//        return ResponseEntity.ok("Product added to cart successfully");// ето тоже работает, но 1 вариант более науны
//    }
    /*query
    POST  http://localhost:8080/cart
    BODY:
    {
      "productId": 5,
      "quantity": 2
    }
     */

    @GetMapping("/items")
    @Operation(summary = "Returns a list of items in a given user's cart selected by user id")
    public List<CartItemsDto> getCartItemsForUser(@RequestHeader("userId") Integer userId) {
        return cartService.getCartItemsForUser(userId);
    }
    /*
    GET request: http://localhost:8080/cart/items
    Header:userId: 1
     */

    @GetMapping("/fullId")
    @Operation(summary = "Returns a full cart of a user selected by cart id")
    public Optional<CartFullDto> getFullCartById(@RequestHeader("cartId") Integer cartId) {
        return Optional.ofNullable(cartService.getFullCartById(cartId));
    }
    /*
    query:  GET   http://localhost:8080/cart/fullId
            HEADER  cartId  1
     */

    @PatchMapping("/changeQuantity")
    @Operation(summary = "changes quantity of items in the given cart")
    public CartFullDto changeCartItemsQuantity(@RequestParam Integer cartId,
                                               @RequestParam Integer cartItemsId,
                                               @RequestParam Integer cartItemsQuantity) {
        return cartService.changeCartItem(cartId, cartItemsId, cartItemsQuantity);
    }
    /*
    PATCH http://localhost:8080/cart/changeQuantity?cartId=5&cartItemsId=5&cartItemsQuantity=1
     */

    @GetMapping("/userId")
    @Operation(summary = "Returns a cart for a given user by user id")
    public Optional<CartFullDto> findCartForUser(@RequestHeader("userId") Integer userId){
        Optional<CartFullDto> find = Optional.ofNullable(cartService.getCartByUserId(userId));
        return find;
    }
}


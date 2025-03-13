package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.AddToCartRequestDto;
import finalproject.onlinegardenshop.dto.CartDto;
import finalproject.onlinegardenshop.dto.CartFullDto;
import finalproject.onlinegardenshop.dto.CartItemsDto;
import finalproject.onlinegardenshop.service.CartService;
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
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/all")
    public List<CartDto> gerAllCarts() {
        return cartService.getAll();
    }

    @GetMapping("/full")
    public List<CartFullDto> getFullCart() {
        return cartService.getAllCartWithItemsAndUsers();
    }

    @GetMapping("{id}")
    public Optional<CartDto> getCartById(@PathVariable Integer id) {
        return Optional.ofNullable(cartService.getCartById(id));
    }

    @PostMapping
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
    public List<CartItemsDto> getCartItemsForUser(@RequestHeader("userId") Integer userId) {
        return cartService.getCartItemsForUser(userId);
    }
    /*
    GET request: http://localhost:8080/cart/items
    Header:userId: 1
     */

    @GetMapping("/fullId")
    public Optional<CartFullDto> getFullCartById(@RequestHeader("cartId") Integer cartId) {
        return Optional.ofNullable(cartService.getFullCartById(cartId));
    }
    /*
    query:  GET   http://localhost:8080/cart/fullId
            HEADER  cartId  1
     */

    @PatchMapping("/changeQuantity")
    public CartFullDto changeCartItemsQuantity(@RequestParam Integer cartId,
                                               @RequestParam Integer cartItemsId,
                                               @RequestParam Integer cartItemsQuantity) {
        return cartService.changeCartItem(cartId, cartItemsId, cartItemsQuantity);
    }
    /*
    PATCH http://localhost:8080/cart/changeQuantity?cartId=5&cartItemsId=5&cartItemsQuantity=1
     */
}


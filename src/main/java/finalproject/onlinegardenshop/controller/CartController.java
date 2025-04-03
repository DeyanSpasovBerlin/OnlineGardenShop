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
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<CartDto> gerAllCarts() {
        return cartService.getAll();
    }

    @GetMapping("/full")
    @Operation(summary = "Returns a list of full carts with cart items and users data")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<CartFullDto> getFullCart() {
        return cartService.getAllCartWithItemsAndUsers();
    }

    @GetMapping("{id}")
    @Operation(summary = "Returns a cart by its id")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
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
    /*query
    POST  http://localhost:8080/cart
    Header:userId: number
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
    Header:userId: number
     */

    @GetMapping("/fullId")
    @Operation(summary = "Returns a full cart of a user selected by cart id")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Optional<CartFullDto> getFullCartById(@RequestHeader("cartId") Integer cartId) {
        return Optional.ofNullable(cartService.getFullCartById(cartId));
    }
    /*
    query:  GET   http://localhost:8080/cart/fullId
            HEADER:  cartId:  number
     */

    @PatchMapping("/changeQuantity")
    @Operation(summary = "changes quantity of items in the given cart")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT')")
    public CartFullDto changeCartItemsQuantity(@RequestParam Integer cartId,
                                               @RequestParam Integer cartItemsId,
                                               @RequestParam Integer cartItemsQuantity){//@RequestHeader("userId") Integer userId
        return cartService.changeCartItem(cartId, cartItemsId, cartItemsQuantity);//,userId
    }
    /*
    PATCH http://localhost:8080/cart/changeQuantity?cartId=5&cartItemsId=5&cartItemsQuantity=1
    Header:userId: number
     */

    @GetMapping("/userId")
    @Operation(summary = "Returns a cart for a given user by user id")
    public Optional<CartFullDto> findCartForUser(@RequestHeader("userId") Integer userId){
        Optional<CartFullDto> find = Optional.ofNullable(cartService.getCartByUserId(userId));
        return find;
    }
        /*
    GET http://localhost:8080/cart/userId
    Header:userId: number
     */
}


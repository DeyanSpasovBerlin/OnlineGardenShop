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
    @Operation(summary = "Returns a list of all carts available in the app for use from ADMIN")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<CartDto> gerAllCarts() {
        return cartService.getAll();
    }

    @GetMapping("/full")
    @Operation(summary = "Returns a list of full carts with cart items and users data for use from ADMIN")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<CartFullDto> getFullCart() {
        return cartService.getAllCartWithItemsAndUsers();
    }

    @GetMapping("{id}")
    @Operation(summary = "Returns a cart by its id for use from ADMIN")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Optional<CartDto> getCartById(@PathVariable Integer id) {
        return Optional.ofNullable(cartService.getCartById(id));
    }

    @PostMapping
    @Operation(summary = "Adds product(s) to a user's cart, from authorized user")
    public ResponseEntity<String> addToCart(@Valid @RequestBody AddToCartRequestDto request) {
        cartService.addToCart(request);
        return ResponseEntity.ok("Product added to cart successfully");
    }

    @GetMapping("/items")
    @Operation(summary = "Returns a list of items in the cart for user, who is authorized")
    public List<CartItemsDto> getCartItemsForUser() {
        return cartService.getCartItemsForUser();
    }

    @GetMapping("/fullItems")
    @Operation(summary = "Returns a list of items in the cart+ cartId + userId  for user, who is authorized")
    public CartFullDto getCartFullItemsForUser() {
        return cartService.getCartFulItemsForUser();
    }

    @GetMapping("/fullId")
    @Operation(summary = "Returns a full cart of a user selected by cart id for use from ADMIN")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Optional<CartFullDto> getFullCartById(@RequestHeader("cartId") Integer cartId) {
        return Optional.ofNullable(cartService.getFullCartById(cartId));
    }

    @PatchMapping("/changeQuantity")
    @Operation(summary = "changes quantity of items in the cart of authorized user")
    public CartFullDto changeCartItemsQuantity(@RequestParam Integer cartItemsId,
                                                  @RequestParam Integer cartItemsQuantity){
        return cartService.changeCartItem(cartItemsId, cartItemsQuantity);
    }

    @GetMapping("/userId")
    @Operation(summary = "Returns the cart of authorized user")
    public Optional<CartFullDto> findCartForUser(){
        Optional<CartFullDto> find = Optional.ofNullable(cartService.getCartByUserId());
        return find;
    }

}


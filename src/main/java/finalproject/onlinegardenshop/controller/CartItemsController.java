package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.CartItemsDto;
import finalproject.onlinegardenshop.service.CartItemsService;
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
@RequestMapping("/cart-items")
@Validated
@Tag(name = "Cart Items Controller", description = "REST API to manage cart items related operations in the app")
public class CartItemsController {

    private final CartItemsService cartItemsService;

    @Autowired
    public CartItemsController(CartItemsService cartItemsService) {
        this.cartItemsService = cartItemsService;
    }

    @GetMapping("/all")
    @Operation(summary = "Returns a list of cart items")
    public List<CartItemsDto> getAllCartItems(){
        return cartItemsService.getAll();
    }

    @GetMapping("{id}")
    @Operation(summary = "Returns cart items by id")
    public Optional<CartItemsDto> getUsersById(@PathVariable Integer id) {
        return Optional.ofNullable(cartItemsService.getCartItemsById(id));
    }

    @PostMapping
    @Operation(summary = "Adds items to a cart")
    public ResponseEntity<String> addToCart(@RequestHeader("usersId") Integer userId,
                                            @Valid @RequestBody CartItemsDto request) {
        cartItemsService.addToCart(userId, request);
        return ResponseEntity.ok("Product added to cart successfully");
    }
}

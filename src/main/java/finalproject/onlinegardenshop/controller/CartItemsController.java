package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.CartItemsDto;
import finalproject.onlinegardenshop.service.CartItemsService;
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
@RequestMapping("/cart-items")
@Validated
@Tag(name = "Cart Items Controller", description = "REST API for emargence use only from ADMIN!")
public class CartItemsController {

    private final CartItemsService cartItemsService;

    @Autowired
    public CartItemsController(CartItemsService cartItemsService) {
        this.cartItemsService = cartItemsService;
    }

    @GetMapping("/all")
    @Operation(summary = "Returns a list of cart items")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<CartItemsDto> getAllCartItems(){
        return cartItemsService.getAll();
    }

    @GetMapping("{id}")
    @Operation(summary = "Returns cart items by id")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Optional<CartItemsDto> getUsersById(@PathVariable Integer id) {
        return Optional.ofNullable(cartItemsService.getCartItemsById(id));
    }

    @PostMapping
    @Operation(summary = "Adds items to a cart")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<String> addToCart(@RequestHeader("usersId") Integer userId,
                                            @Valid @RequestBody CartItemsDto request) {
        cartItemsService.addToCart(userId, request);
        return ResponseEntity.ok("Product added to cart successfully");
    }
}

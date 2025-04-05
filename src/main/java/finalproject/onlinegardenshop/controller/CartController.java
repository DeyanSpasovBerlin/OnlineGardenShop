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
    public ResponseEntity<String> addToCart(//@RequestHeader("userId") Integer userId,//if no Security leer add header
                                            @Valid @RequestBody AddToCartRequestDto request) {
        cartService.addToCart(request);//if we use header add second arg: userId,
        return ResponseEntity.ok("Product added to cart successfully");
    }
    /*query
    POST  http://localhost:8080/cart
    BODY:
    {
      "productId": 5,
      "quantity": 2
    }
     */

    @GetMapping("/items")
    @Operation(summary = "Returns a list of items in the cart for user, who is authorized")
    public List<CartItemsDto> getCartItemsForUser() {//if no Security leer add header  @RequestHeader("userId") Integer userId
        return cartService.getCartItemsForUser();//if we use header add second arg: userId,
    }
    /*
    GET request: http://localhost:8080/cart/items
     */

    @GetMapping("/fullItems")
    @Operation(summary = "Returns a list of items in the cart+ cartId + userId  for user, who is authorized")
    public CartFullDto getCartFullItemsForUser() {//if no Security leer add header  @RequestHeader("userId") Integer userId
        return cartService.getCartFulItemsForUser();//if we use header add second arg: userId,
    }
    /*
    GET request: http://localhost:8080/cart/fullItems
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
    @Operation(summary = "changes quantity of items in the cart of authorized user")
    public CartFullDto changeCartItemsQuantity(@RequestParam Integer cartItemsId,
                                                  @RequestParam Integer cartItemsQuantity){
        return cartService.changeCartItem(cartItemsId, cartItemsQuantity);
    }
    //PATCH http://localhost:8080/cart/changeQuantity?cartItemsId=5&cartItemsQuantity=1

    @GetMapping("/userId")
    @Operation(summary = "Returns the cart of authorized user")
    public Optional<CartFullDto> findCartForUser(){//if no Security leer add header  @RequestHeader("userId") Integer userId
        Optional<CartFullDto> find = Optional.ofNullable(cartService.getCartByUserId());
        return find;
    }
        /*
    GET http://localhost:8080/cart/userId
     */
}


package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.CartItemsDto;
import finalproject.onlinegardenshop.dto.UsersDto;
import finalproject.onlinegardenshop.service.CartItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cart-items")
@Validated
public class CartItemsController {

    private final CartItemsService cartItemsService;

    @Autowired
    public CartItemsController(CartItemsService cartItemsService) {
        this.cartItemsService = cartItemsService;
    }

    @GetMapping("/all")
    public List<CartItemsDto> getAllCartItems(){
        return cartItemsService.getAll();
    }

    @GetMapping("{id}")
    public Optional<CartItemsDto> getUsersById(@PathVariable Integer id) {
        return Optional.ofNullable(cartItemsService.getCartItemsById(id));
    }
}

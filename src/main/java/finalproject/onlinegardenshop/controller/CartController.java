package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.CartDto;
import finalproject.onlinegardenshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
@Validated
public class CartController {
    private CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/all")
    public List<CartDto> gerAllCarts(){
        return cartService.getAll();
    }

    @GetMapping("{id}")
    public Optional<CartDto> getCartById(@PathVariable Integer id) {
        return Optional.ofNullable(cartService.getCartById(id));
    }
}

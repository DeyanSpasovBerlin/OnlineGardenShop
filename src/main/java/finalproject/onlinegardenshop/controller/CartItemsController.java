package finalproject.onlinegardenshop.controller;


import finalproject.onlinegardenshop.dto.CartItemstDto;
import finalproject.onlinegardenshop.service.CartItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cart-items")
@Validated
public class CartItemsController {

    private final CartItemsService cartItemsService;

    @Autowired
    public CartItemsController(CartItemsService cartItemsService) {
        this.cartItemsService = cartItemsService;
    }

    public List<CartItemstDto> getAllCartItems(){
        return cartItemsService.getAll();
    }
}

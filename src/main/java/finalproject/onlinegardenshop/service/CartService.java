package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.CartDto;
import finalproject.onlinegardenshop.entity.Cart;
import finalproject.onlinegardenshop.entity.Users;
import finalproject.onlinegardenshop.mapper.CartMapper;
import finalproject.onlinegardenshop.repository.CartRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CartService {

    private static Logger logger = LogManager.getLogger(UsersService.class);

    private final CartRepository cartRepository;

    private final CartMapper cartMapper;

    @Autowired
    public CartService(CartRepository cartRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    public List<CartDto> getAll(){
        List<Cart> carts = cartRepository.findAll();
        logger.debug("Carts retrieved from db");
        logger.debug("cart ids: {}", () -> carts.stream().map(Cart::getId).toList());
        return cartMapper.entityListToDto(carts);
    }
}

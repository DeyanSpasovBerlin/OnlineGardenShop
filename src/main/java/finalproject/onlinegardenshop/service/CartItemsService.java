package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.UsersDto;
import finalproject.onlinegardenshop.entity.CartItems;
import finalproject.onlinegardenshop.entity.Users;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.mapper.CartItemsMapper;
import finalproject.onlinegardenshop.repository.CartItemsRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import finalproject.onlinegardenshop.dto.CartItemsDto;


import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CartItemsService {

    private static Logger logger = LogManager.getLogger(CartItemsService.class);

    private final CartItemsMapper cartItemsMapper;
    private final CartItemsRepository cartItemsRepository;

    @Autowired
    public CartItemsService(CartItemsMapper cartItemsMapper, CartItemsRepository cartItemsRepository) {
        this.cartItemsMapper = cartItemsMapper;
        this.cartItemsRepository = cartItemsRepository;
    }

    public List<CartItemsDto> getAll(){
        List<CartItems> cartItems = cartItemsRepository.findAll();
        logger.debug("CartItems retrieved from db");
        logger.debug("CartItems ids: {}", () -> cartItems.stream().map(CartItems::getId).toList());
        return cartItemsMapper.entityListToDto(cartItems);
    }

    public CartItemsDto getCartItemsById(Integer id) {
        Optional<CartItems> optional = cartItemsRepository.findById(id);
        if (optional.isPresent()) {
            CartItemsDto found = cartItemsMapper.entityToDto(optional.get()) ;
            return found;
        }
        throw new OnlineGardenShopResourceNotFoundException("CartItems with id = " + id + " not found in database");
    }

}

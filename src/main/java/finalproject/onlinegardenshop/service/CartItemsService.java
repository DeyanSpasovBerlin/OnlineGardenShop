package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.entity.Cart;
import finalproject.onlinegardenshop.entity.CartItems;
import finalproject.onlinegardenshop.entity.Products;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.mapper.CartItemsMapper;
import finalproject.onlinegardenshop.repository.CartItemsRepository;
import finalproject.onlinegardenshop.repository.CartRepository;
import finalproject.onlinegardenshop.repository.ProductsRepository;
import finalproject.onlinegardenshop.repository.UsersRepository;
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

    @Autowired
    private final CartItemsMapper cartItemsMapper;
    private final CartItemsRepository cartItemsRepository;
    private final CartRepository cartRepository;
    private final ProductsRepository productRepository;
    private final UsersRepository userRepository;

    public CartItemsService(CartItemsMapper cartItemsMapper,
                            CartItemsRepository cartItemsRepository,
                            CartRepository cartRepository,
                            ProductsRepository productRepository,
                            UsersRepository userRepository) {
        this.cartItemsMapper = cartItemsMapper;
        this.cartItemsRepository = cartItemsRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
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

    @Transactional
    public void addToCart(Integer userId, CartItemsDto request) {
        Products product = productRepository.findById(request.getProductsId())
                .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("Product not found"));

        Cart cart = cartRepository.findById(userId)//findByUsersId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUsers(userRepository.findById(userId)
                            .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("User not found")));
                    return cartRepository.save(newCart);
                });

        CartItems cartItem = new CartItems();
        cartItem.setCart(cart);
        cartItem.setProducts(product);
        cartItem.setQuantity(request.getQuantity());

        cartItemsRepository.save(cartItem);
    }

}

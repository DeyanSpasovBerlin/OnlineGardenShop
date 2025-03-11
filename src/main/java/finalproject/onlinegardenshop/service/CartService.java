package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.AddToCartRequestDto;
import finalproject.onlinegardenshop.dto.CartDto;
import finalproject.onlinegardenshop.dto.CartFullDto;
import finalproject.onlinegardenshop.dto.CartItemsDto;
import finalproject.onlinegardenshop.entity.Cart;
import finalproject.onlinegardenshop.entity.CartItems;
import finalproject.onlinegardenshop.entity.Products;
import finalproject.onlinegardenshop.entity.Users;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.mapper.CartItemsMapper;
import finalproject.onlinegardenshop.mapper.CartMapper;
import finalproject.onlinegardenshop.repository.CartItemsRepository;
import finalproject.onlinegardenshop.repository.CartRepository;
import finalproject.onlinegardenshop.repository.ProductsRepository;
import finalproject.onlinegardenshop.repository.UsersRepository;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CartService {

    private static Logger logger = LogManager.getLogger(CartService.class);

    private final CartRepository cartRepository;
    private final CartItemsRepository cartItemsRepository;
    private final ProductsRepository productsRepository;
    private final CartMapper cartMapper;
    private final UsersRepository usersRepository;
    private final CartItemsMapper cartItemsMapper;

    @Autowired
    public CartService(CartRepository cartRepository,
                       CartItemsRepository cartItemsRepository,
                       ProductsRepository productsRepository,
                       CartMapper cartMapper,
                       UsersRepository usersRepository,
                       CartItemsMapper cartItemsMapper) {
        this.cartRepository = cartRepository;
        this.cartItemsRepository = cartItemsRepository;
        this.productsRepository = productsRepository;
        this.cartMapper = cartMapper;
        this.usersRepository = usersRepository;
        this.cartItemsMapper = cartItemsMapper;
    }

    public List<CartDto> getAll(){
        List<Cart> carts = cartRepository.findAll();
        logger.debug("Carts retrieved from db");
        logger.debug("cart ids: {}", () -> carts.stream().map(Cart::getId).toList());
        return cartMapper.entityListToDto(carts);
    }

    public CartDto getCartById(Integer id) {
        Optional<Cart> optional = cartRepository.findById(id);
        if (optional.isPresent()) {
            CartDto found = cartMapper.entityToDto(optional.get()) ;
            return found;
        }
        throw new OnlineGardenShopResourceNotFoundException("Cart with id = " + id + " not found in database");
    }

    @Transactional
    public void addToCart(Integer userId, AddToCartRequestDto request) {
        logger.info("Adding product {} to cart for user {}", request.getProductId(), userId);
        // Check if product exists
        Products product = productsRepository.findById(request.getProductId())
                .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("Product not found"));
        // Find user's cart or create one
        Cart cart = cartRepository.findByUsersId(userId)//.findByUsersIdAndCompletedFalse(userId) - если будем пользоваться флаг
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    Users user = usersRepository.findById(userId)
                            .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("User not found"));
                    newCart.setUsers(user);  // Assign the User object
                    Cart savedCart = cartRepository.save(newCart);
                    logger.debug("New Cart Saved: {}", savedCart.getId());  // Add a log to see if the cart is saved
                    return savedCart;  // Save the new cart to the database
                });
        // Check if the product is already in the cart
        Optional<CartItems> existingCartItem = cartItemsRepository.findByCartIdAndProductsId(cart.getId(), product.getId());
        if (existingCartItem.isPresent()) {
            // Product already in cart → Update quantity
            CartItems cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
            cartItemsRepository.save(cartItem);
        } else {
            // New product → Add to cart
            CartItems newCartItem = new CartItems();
            newCartItem.setCart(cart);
            newCartItem.setProducts(product);
            newCartItem.setQuantity(request.getQuantity());
            cartItemsRepository.save(newCartItem);
        }
        logger.info("Product {} added to cart for user {}", request.getProductId(), userId);
    }

    public List<CartItemsDto> getCartItemsForUser(Integer userId) {
        Cart cart = cartRepository.findByUsersId(userId)
                .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("Cart not found"));
        return cartItemsMapper.entityListToDto(cart.getCartItems());
    }

    public List<CartFullDto> getAllCartWithItemsAndUsers(){
        List<Cart> carts = cartRepository.findAll();
        return cartMapper.entityFullListToDto(carts);
    }

}

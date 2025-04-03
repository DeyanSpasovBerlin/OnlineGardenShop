package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.AddToCartRequestDto;
import finalproject.onlinegardenshop.dto.CartDto;
import finalproject.onlinegardenshop.dto.CartFullDto;
import finalproject.onlinegardenshop.dto.CartItemsDto;
import finalproject.onlinegardenshop.entity.Cart;
import finalproject.onlinegardenshop.entity.CartItems;
import finalproject.onlinegardenshop.entity.Products;
import finalproject.onlinegardenshop.entity.Users;
import finalproject.onlinegardenshop.entity.enums.UserRole;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users authorizedUser = usersRepository.findByEmail((String) auth.getPrincipal()).get();//find user, who is authorized
        // Ако ролята е CLIENT, проверяваме дали иска собственото си ID
        if (authorizedUser.getRole() == UserRole.CLIENT && !authorizedUser.getId().equals(userId)) {
            throw new AccessDeniedException("Clients can only access their own data.");
        }
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users existingUser = usersRepository.findByEmail((String) auth.getPrincipal()).get();//find user, who is authorized
        // Ако ролята е CLIENT, проверяваме дали иска собственото си ID
        if (existingUser.getRole() == UserRole.CLIENT && !existingUser.getId().equals(userId)) {
            throw new AccessDeniedException("Clients can only access their own data.");
        }
        Cart cart = cartRepository.findByUsersId(userId)
                .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("Cart not found"));
        return cartItemsMapper.entityListToDto(cart.getCartItems());
    }

    public List<CartFullDto> getAllCartWithItemsAndUsers(){
        List<Cart> carts = cartRepository.findAll();
        return cartMapper.entityFullListToDto(carts);
    }

    public CartFullDto getFullCartById(Integer id) {
        Optional<Cart> optional = cartRepository.findById(id);
        if (optional.isPresent()) {
            CartFullDto found = cartMapper.entityToFullDto(optional.get());
            return found;
        }
        throw new OnlineGardenShopResourceNotFoundException("Cart with id = " + id + " not found in database");
    }


    @Transactional
    public CartFullDto changeCartItem(Integer cartId, Integer itemId, Integer quantity) {//,Integer userId
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users authorizedUser = usersRepository.findByEmail((String) auth.getPrincipal()).get();//find user, who is authorized
        Optional<Cart> optional = cartRepository.findById(cartId);
        if (optional.isEmpty()) {
            throw new OnlineGardenShopResourceNotFoundException("Cart with id = " + cartId +
                    " hasn’t CartItems with id: " + itemId);
        }
        Cart cart = optional.get(); // Work directly with the entity
        //сравняваме дали user ID from cart == user ID from Authentication
        if(cart.getUsers().getId() != authorizedUser.getId()){
            throw new AccessDeniedException("Clients can only access their own data.");
        }
        boolean itemFound = false;
        for (CartItems item : cart.getCartItems()) {
            if (item.getId().equals(itemId)) {
                item.setQuantity(quantity);
                cartItemsRepository.save(item); // Persist changes to CartItems
                itemFound = true;
                break;
            }
        }
        if (!itemFound) {
            throw new OnlineGardenShopResourceNotFoundException("Cart with id = " + cartId +
                    " hasn’t CartItems with id: " + itemId);
        }
        // Save cart to persist changes
        cartRepository.save(cart);

        return cartMapper.entityToFullDto(cart); // Convert back to DTO after changes are saved
    }

    public CartFullDto getCartByUserId(Integer userId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users authorizedUser = usersRepository.findByEmail((String) auth.getPrincipal()).get();//find user, who is authorized
        // Ако ролята е CLIENT, проверяваме дали иска собственото си ID
        if (authorizedUser.getRole() == UserRole.CLIENT && !authorizedUser.getId().equals(userId)) {
            throw new AccessDeniedException("Clients can only access their own data.");
        }
         Optional<Cart> optional = cartRepository.findByUsersId(userId);
         if(optional.isPresent()){
             CartFullDto find = cartMapper.entityToFullDto(optional.get());
             return find;
         }else {
             throw new OnlineGardenShopResourceNotFoundException("Cart fot User with id = " + userId + " not found in database");
         }

    }

}

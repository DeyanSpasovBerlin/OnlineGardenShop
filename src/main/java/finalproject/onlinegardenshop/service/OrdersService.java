package finalproject.onlinegardenshop.service;


import finalproject.onlinegardenshop.dto.CreateOrderRequestDto;
import finalproject.onlinegardenshop.dto.OrdersDto;
import finalproject.onlinegardenshop.entity.*;
import finalproject.onlinegardenshop.entity.enums.DeliveryMethod;
import finalproject.onlinegardenshop.entity.enums.OrdersStatus;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.mapper.OrdersMapper;
import finalproject.onlinegardenshop.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrdersService {

    private static Logger logger = LogManager.getLogger(OrdersService.class);

    private final OrdersMapper ordersMapper;
    private final OrdersRepository ordersRepository;
    private final UsersRepository usersRepository;
    private final ProductsRepository productsRepository;
    private final CartRepository cartRepository;
    private final CartItemsRepository cartItemsRepository;

    @Autowired
    public OrdersService(OrdersMapper ordersMapper,
                         OrdersRepository ordersRepository,
                         UsersRepository usersRepository,
                         ProductsRepository productsRepository,
                         CartRepository cartRepository,
                         CartItemsRepository cartItemsRepository) {
        this.ordersMapper = ordersMapper;
        this.ordersRepository = ordersRepository;
        this.usersRepository = usersRepository;
        this.productsRepository = productsRepository;
        this.cartRepository = cartRepository;
        this.cartItemsRepository = cartItemsRepository;
    }

public Page<OrdersDto> getAllOrders(Pageable pageable, String sortBy, String direction) {
    if (sortBy != null && direction != null) {
        String[] sortByFields = sortBy.split(",");
        String[] directions = direction.split(",");
        if (sortByFields.length != directions.length) {
            throw new IllegalArgumentException("Number of sortBy fields must match number of direction fields");
        }
        List<Sort.Order> orders = new ArrayList<>();
        for (int i = 0; i < sortByFields.length; i++) {
            Sort.Direction dir = directions[i].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            orders.add(Sort.Order.by(sortByFields[i]).with(dir));
        }
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(orders));
    }
    Page<Orders> ordersPage = ordersRepository.findAll(pageable);
    return ordersPage.map(ordersMapper::entityToDto);
}

    public OrdersDto getOrderssById(Integer id) {
        Optional<Orders> optional = ordersRepository.findById(id);
        if (optional.isPresent()) {
            OrdersDto found = ordersMapper.entityToDto(optional.get()) ;
            return found;
        }
        throw new OnlineGardenShopResourceNotFoundException("Orders with id = " + id + " not found in database");
    }

    @Transactional
    public OrdersDto createOrder(CreateOrderRequestDto request) {
        Orders order = new Orders();
        order.setDeliveryAddress(request.getDeliveryAddress());
        DeliveryMethod deliveryMethod = DeliveryMethod.valueOf(request.getDeliveryMethod().toUpperCase());
        order.setDeliveryMethod(deliveryMethod);
        order.setStatus(OrdersStatus.CREATED);
        if (request.getUsersId() != null) {
            Users user = usersRepository.findById(request.getUsersId())
                    .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("User not found"));
            order.setUsers(user);
            order.setContactPhone(user.getPhone());
        }
        List<OrderItems> orderItems = request.getItems().stream().map(itemDto -> {
            OrderItems orderItem = new OrderItems();
            Products product = productsRepository.findById(Integer.parseInt(itemDto.getProductId()))
                    .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("Product not found"));
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice());
            return orderItem;
        }).toList();
        order.setOrderItems(orderItems);
        Orders savedOrder = ordersRepository.save(order);
        logger.info("Created new order with ID: {}", savedOrder.getId());
        return ordersMapper.entityToDto(savedOrder);
    }

    @Transactional
    public void checkout(String deliveryAddress,String contactPhone,String deliveryMethod) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users authorizedUser = usersRepository.findByEmail((String) auth.getPrincipal()).get();
        Integer authorizedUserId = authorizedUser.getId();
        logger.info("Checkout initiated for user with name: {} and ID: {}", authorizedUser.getFirstName(), authorizedUserId);
        Cart cart = cartRepository.findByUsersId(authorizedUserId)
                .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("Cart not found"));
        if (cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty. Cannot checkout.");
        }
        Orders order = new Orders();
        order.setUsers(cart.getUsers());
        order.setUpdatedAt(LocalDateTime.now());
        order.setStatus(OrdersStatus.PENDING_PAYMENT);
        order.setDeliveryAddress(deliveryAddress);
        order.setContactPhone(contactPhone);
        DeliveryMethod deliveryMethodEnum = DeliveryMethod.valueOf(deliveryMethod.toUpperCase());
        order.setDeliveryMethod(deliveryMethodEnum);
        order.setTotalPrice(calculateTotal(cart));
        for (CartItems cartItem : cart.getCartItems()) {
            logger.info("Processing cart item: Product ID = {}, Quantity = {}",
                    cartItem.getProducts().getId(), cartItem.getQuantity());
            OrderItems orderItem = new OrderItems();
            orderItem.setProduct(cartItem.getProducts());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(cartItem.getProducts().getPrice());
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }
        logger.info("Saving order for user with name: {} and ID: {}", authorizedUser.getFirstName(), authorizedUserId);
        ordersRepository.save(order);
        logger.info("Clearing cart for user with name: {} and ID: {}", authorizedUser.getFirstName(), authorizedUserId);
        cartItemsRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
        cartRepository.save(cart);
        logger.info("Checkout completed successfully for user" +
                " with name: {} and ID: {}", authorizedUser.getFirstName(), authorizedUserId);
    }

    private Double calculateTotal(Cart cart) {
        return cart.getCartItems().stream()
                .mapToDouble(cartItem -> cartItem.getProducts().getPrice() * cartItem.getQuantity())
                .sum();
    }

    @Transactional
    public List<OrdersDto> getOrdersByUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users authorizedUser = usersRepository.findByEmail((String) auth.getPrincipal()).get();
        Integer authorizedUserId = authorizedUser.getId();
        List<Orders> orders = ordersRepository.findByUsersId(authorizedUserId);
        if(orders.isEmpty()){
            throw new OnlineGardenShopResourceNotFoundException("Orders for user with id = "
                    + authorizedUserId + " and first name= "+authorizedUser.getFirstName()+
                    " not found in database");
        }
        return ordersMapper.entityListToDto(orders);
    }

    @Transactional
    public List<OrdersDto> getOrdersByUserFromAdmin(Integer userId){
        List<Orders> orders = ordersRepository.findByUsersId(userId);
        if(orders.isEmpty()){
            throw new OnlineGardenShopResourceNotFoundException("Orders for user with id = " + userId + " not found in database");
        }
        return ordersMapper.entityListToDto(orders);
    }

    @Transactional
    public void cancelOrdersStatusByAdmin(Integer id) {
        OrdersStatus canceled = OrdersStatus.valueOf("CANCELED");
        Optional<Orders> optional = ordersRepository.findById(id);
        if (optional.isPresent()) {
            Orders find = optional.get();
            OrdersStatus findStatus = find.getStatus();
            if (findStatus == OrdersStatus.CREATED || findStatus == OrdersStatus.PENDING_PAYMENT) {
                find.setStatus(canceled);
                Orders saved = ordersRepository.save(find);
                ordersMapper.entityToDto(saved);
            } else {
                throw new OnlineGardenShopResourceNotFoundException("Order with id = " + id + " can not be canceled!");
            }
        }else{
            throw new OnlineGardenShopResourceNotFoundException("Order with id = " + id + " not found in database");
        }
    }

    @Transactional
    public void cancelOrdersStatusByUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users authorizedUser = usersRepository.findByEmail((String) auth.getPrincipal()).get();
        Integer authorizedUserId = authorizedUser.getId();
        OrdersStatus canceled = OrdersStatus.valueOf("CANCELED");
        List<Orders> listOrders = ordersRepository.findByUsersId(authorizedUserId);
        if(listOrders.isEmpty()) throw new OnlineGardenShopResourceNotFoundException("User with id = " + authorizedUserId+
                " and first name: "+authorizedUser.getFirstName()+
                " have no any orders.");
        Orders lastOrder = listOrders.getLast();
        OrdersStatus findStatus = lastOrder.getStatus();
            if (findStatus == OrdersStatus.CREATED || findStatus == OrdersStatus.PENDING_PAYMENT) {
                lastOrder.setStatus(canceled);
                Orders saved = ordersRepository.save(lastOrder);
                ordersMapper.entityToDto(saved);
            } else {
                throw new OnlineGardenShopResourceNotFoundException("Order for user with id = "
                        + authorizedUserId + " and first name: "
                        +authorizedUser.getFirstName()+" can not be canceled!");
            }
    }

    public OrdersDto getLastOrdersByUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users authorizedUser = usersRepository.findByEmail((String) auth.getPrincipal()).get();
        Integer authorizedUserId = authorizedUser.getId();
        List<Orders> listOrders = ordersRepository.findByUsersId(authorizedUserId);
        if(listOrders.isEmpty()) throw new OnlineGardenShopResourceNotFoundException("User with id = " + authorizedUserId+
                " and first name: "+authorizedUser.getFirstName()+
                " have no any orders.");
        Orders lastOrder = listOrders.getLast();
        return ordersMapper.entityToDto(lastOrder);
    }

    public List<OrdersDto> getOrdersByDeletedUser(Integer userId) {
        List<Orders> orders = ordersRepository.findByDeletedUserId(-userId);
        List<OrdersDto> ordersDtos = ordersMapper.entityListToDto(orders);
        return ordersDtos;
    }

    public List<OrdersDto> getAllDeletedUsersOrders() {
        List<Orders> orders = ordersRepository.findAllByDeletedUserIdIsNotNull();
        List<OrdersDto> ordersDtos = ordersMapper.entityListToDto(orders);
        return ordersDtos;
    }

}








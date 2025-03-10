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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    public List<OrdersDto> getAll(){
        List<Orders> orders = ordersRepository.findAll();
        logger.debug("Orders retrieved from db");
        logger.debug("Orders ids: {}", () -> orders.stream().map(Orders::getId).toList());
        return ordersMapper.entityListToDto(orders);
    }

    public OrdersDto getOrderssById(Integer id) {
        Optional<Orders> optional = ordersRepository.findById(id);
        if (optional.isPresent()) {
            OrdersDto found = ordersMapper.entityToDto(optional.get()) ;
            return found;
        }
        throw new OnlineGardenShopResourceNotFoundException("Orders with id = " + id + " not found in database");
    }

    // REST API from tex docs:
    //    1 •	•	Оформление заказа  ->   service
    @Transactional
    public OrdersDto createOrder(CreateOrderRequestDto request) {
        Orders order = new Orders();
        order.setDeliveryAddress(request.getDeliveryAddress());
        // Convert String to Enum (safe conversion)
        DeliveryMethod deliveryMethod = DeliveryMethod.valueOf(request.getDeliveryMethod().toUpperCase());
        order.setDeliveryMethod(deliveryMethod);
        order.setStatus(OrdersStatus.CREATED); // Default status
        // Fetch the user from the database
        if (request.getUsersId() != null) {
            Users user = usersRepository.findById(request.getUsersId())
                    .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("User not found"));
            order.setUsers(user);
            order.setContactPhone(user.getPhone()); // Set contact phone from user
        }
        // Create OrderItems
        List<OrderItems> orderItems = request.getItems().stream().map(itemDto -> {
            OrderItems orderItem = new OrderItems();
            Products product = productsRepository.findById(Integer.parseInt(itemDto.getProductId()))
                    .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("Product not found"));
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice()); // Capture the price at the time of purchase
            return orderItem;
        }).toList();
        order.setOrderItems(orderItems); // Attach orderItems to order
        Orders savedOrder = ordersRepository.save(order);
        logger.info("Created new order with ID: {}", savedOrder.getId());
        return ordersMapper.entityToDto(savedOrder);
    }

    @Transactional
    public void checkout(Integer userId) {
//        Double sum = (double) 0;
        Cart cart = cartRepository.findByUsersId(userId)
                .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("Cart not found"));
        if (cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty. Cannot checkout.");
        }
        Orders order = new Orders();
        order.setUsers(cart.getUsers());
        order.setUpdatedAt(LocalDateTime.now());
        order.setStatus(OrdersStatus.PENDING_PAYMENT);
        order.setTotalPrice(calculateTotal(cart)); // This should calculate the total of cart items
        // Loop through the cart items and create order items
        for (CartItems cartItem : cart.getCartItems()) {
            OrderItems orderItem = new OrderItems();
            orderItem.setProduct(cartItem.getProducts());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(cartItem.getProducts().getPrice());  // Store price at the time of purchase
//            sum = sum + cartItem.getProducts().getPrice();
            orderItem.setOrder(order);  // Set the order reference
            order.getOrderItems().add(orderItem);
        }
//        order.setTotalPrice(sum);
        // Save the order to the database
        ordersRepository.save(order);
        // Optionally, clear the cart
        cartItemsRepository.deleteAll(cart.getCartItems());//уничтожаем картб плохо для статистики
        // Or just set a "completed" flag on the cart - сохраняем для статистики
//        // Mark cart as completed //ето правильной вариант, но надо добавить колумн в карт!
//        cart.setCompleted(true);
        cartRepository.save(cart);
    }

    private Double calculateTotal(Cart cart) {
        return cart.getCartItems().stream()
                .mapToDouble(cartItem -> cartItem.getProducts().getPrice() * cartItem.getQuantity())
                .sum();
    }

}







/*
List<OrderItem> orderItems = request.getItems().stream().map(itemDto -> {
            OrderItem orderItem = new OrderItem();
            Products product = productsRepository.findById(Integer.parseInt(itemDto.getProductId()))
                    .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("Product not found"));
---------------
Step 1: Getting the List of Items from the Request

List<OrderItem> orderItems = request.getItems().stream()

    request.getItems() gets the list of items from the POST request body.
    .stream() converts the list into a stream to process each item one by one.
************
Step 2: Mapping Each Item from DTO to Entity

.map(itemDto -> {
    OrderItem orderItem = new OrderItem();
******************
Step 3: Finding the Product in the Database

Products product = productsRepository.findById(Integer.parseInt(itemDto.getProductId()))
    .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("Product not found"));

    itemDto.getProductId() gets the product ID from the request.
    Integer.parseInt(...) converts it from String to Integer.
    productsRepository.findById(...) looks for the product in the database.
    .orElseThrow(...) throws an error if the product does not exist.
****************
Why Do We Do This?

We need to convert the productId (which is just a number in JSON) into a full Products object so we can store it in OrderItem.
****************
Example Request
🔹 JSON Request Body

{
  "items": [
    {
      "productId": "1",
      "quantity": 2
    }
  ]
}

🔹 What Happens?

    The system reads productId = "1".
    It converts "1" into an Integer.
    It searches for Products in the database using productsRepository.findById(1).
    If the product exists, it continues.
    If the product does not exist, it throws "Product not found" error.
*******************
Final Thoughts

    This ensures that the product exists before adding it to the order.
    It prevents errors when someone tries to order a non-existent product.
    It links OrderItem correctly to the actual product in the database.
******************

 */
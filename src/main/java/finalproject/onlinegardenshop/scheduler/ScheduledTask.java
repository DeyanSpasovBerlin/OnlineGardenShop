package finalproject.onlinegardenshop.scheduler;

import finalproject.onlinegardenshop.entity.Cart;
import finalproject.onlinegardenshop.repository.CartItemsRepository;
import finalproject.onlinegardenshop.repository.CartRepository;
import finalproject.onlinegardenshop.repository.OrdersRepository;
import finalproject.onlinegardenshop.service.OrdersService;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Log4j2
public class ScheduledTask {
    private static Logger logger = LogManager.getLogger(ScheduledTask.class);

    CartRepository  cartRepository;
    CartItemsRepository cartItemsRepository;
    private final OrdersRepository ordersRepository;

    @Autowired
    public ScheduledTask(OrdersRepository ordersRepository,
                         CartItemsRepository cartItemsRepository,
                         CartRepository cartRepository) {
        this.ordersRepository = ordersRepository;
        this.cartItemsRepository = cartItemsRepository;
        this.cartRepository = cartRepository;
    }
    /*
    Общая идея: добавляем lastUpdated колона Cart entity. Ето поле обновляеться при каждое добавление продукта
    Проверяем прошли ли больше 10 минут от момента now() и lastUpdated
    если ето да  то делаем  delete CartItems
    что бы ето работало в liquibase добавляем 005_data.sql
     */
    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES)
    @Transactional
    public void emptyCartAfterTenMinutes() {

        LocalDateTime now = LocalDateTime.now(); // Declare 'now' here
        List<Cart> carts = cartRepository.findAll();
        for (Cart cart : carts) {
            if (cart.getCartItems().isEmpty()) {// Check if the cart is empty
                continue;
            }
            // Check if last update was more than 10 minutes ago
            if (cart.getLastUpdated() != null && ChronoUnit.MINUTES.between(cart.getLastUpdated(), now) < 10) {
                continue;
            }
            // If conditions are met, clear cart items
            cartItemsRepository.deleteAll(cart.getCartItems());
            cart.getCartItems().clear();
            cartRepository.save(cart);
            logger.info("Cart items deleted successfully for cart ID: {}", cart.getId());
        }
    }
}


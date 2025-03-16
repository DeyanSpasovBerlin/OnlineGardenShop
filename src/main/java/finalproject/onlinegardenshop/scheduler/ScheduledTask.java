package finalproject.onlinegardenshop.scheduler;

import finalproject.onlinegardenshop.entity.Cart;
import finalproject.onlinegardenshop.entity.Orders;
import finalproject.onlinegardenshop.entity.enums.OrdersStatus;
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

import static finalproject.onlinegardenshop.entity.enums.OrdersStatus.*;

@Component
@Log4j2
public class ScheduledTask {
    private static Logger logger = LogManager.getLogger(ScheduledTask.class);

    private final CartRepository  cartRepository;
    private final CartItemsRepository cartItemsRepository;
    private final OrdersRepository ordersRepository;
//    private final OrdersStatus currentStatus;

    @Autowired
    public ScheduledTask(CartRepository cartRepository,
                         CartItemsRepository cartItemsRepository,
                         OrdersRepository ordersRepository) {
        this.cartRepository = cartRepository;
        this.cartItemsRepository = cartItemsRepository;
        this.ordersRepository = ordersRepository;
    }

    /*
    Общая идея: добавляем lastUpdated колона Cart entity. Ето поле обновляеться при каждое добавление продукта
    Проверяем прошли ли больше 10 минут от момента now() и lastUpdated
    если ето да  то делаем  delete CartItems
    что бы ето работало в liquibase добавляем 005_data.sql
     */
    @Scheduled(fixedRate = 50, timeUnit = TimeUnit.MINUTES, initialDelay = 50)
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

    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.SECONDS, initialDelay = 120) // Runs every 30 seconds
    @Transactional
    public void updateOrderStatuses() {
        log.info("Running order status update task...");

        List<Orders> orders = ordersRepository.findAll(); // Retrieve all orders
        for (Orders order : orders) {
            OrdersStatus nextStatus = getNextStatus(order.getStatus());
            if (nextStatus != null) {
                order.setStatus(nextStatus);
                ordersRepository.save(order);
                log.info("Updated order {} status to {}", order.getId(), nextStatus);
            }
        }
    }
    /*
    Идея- все статусы меняются от CREATED->PENDING_PAYMENT->PAID->DELIVERED
    В конце все Orders кроме CANCELED превращаются в  DELIVERED
     */
        private OrdersStatus getNextStatusPartChange(OrdersStatus currentStatus) {
            switch (currentStatus) {
                case CREATED:
                    return PENDING_PAYMENT;
                case PENDING_PAYMENT:
                    return OrdersStatus.PAID;
                case PAID:
                    return SHIPPED;
                case SHIPPED:
                    return OrdersStatus.DELIVERED;
                default:
                    return null; // No change for DELIVERED or CANCELED
            }
        }
    /*
    Идея- все статусы меняются от CREATED->PENDING_PAYMENT->PAID->DELIVERED->CANCELED->CREATED
    согласно:
    CREATED,            // ordinal() returns 0
    PENDING_PAYMENT,    // ordinal() returns 1
    PAID,               // ordinal() returns 2
    SHIPPED,            // ordinal() returns 3
    DELIVERED,          // ordinal() returns 4
    CANCELED            // ordinal() returns 5
    Проверено, что cancelOrdersStatus(Integer id) работает только на
    CREATED,            // ordinal() returns 0
    PENDING_PAYMENT,    // ordinal() returns 1
     */
    private OrdersStatus getNextStatus(OrdersStatus currentStatus) {
        OrdersStatus[] statuses = OrdersStatus.values(); // Get all possible statuses
        int currentIndex = currentStatus.ordinal(); // Get the index of the current status
        int nextIndex = (currentIndex + 1) % statuses.length; // Move to the next status cyclically
        return statuses[nextIndex]; // Return the next status
    }
}


package finalproject.onlinegardenshop.scheduler;

import finalproject.onlinegardenshop.entity.Cart;
import finalproject.onlinegardenshop.entity.Orders;
import finalproject.onlinegardenshop.entity.enums.OrdersStatus;
import finalproject.onlinegardenshop.repository.CartItemsRepository;
import finalproject.onlinegardenshop.repository.CartRepository;
import finalproject.onlinegardenshop.repository.OrdersRepository;
import finalproject.onlinegardenshop.service.EmailService;
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
   private final EmailService emailService;

    @Autowired
    public ScheduledTask(CartRepository cartRepository,
                         CartItemsRepository cartItemsRepository,
                         OrdersRepository ordersRepository,
                         EmailService emailService) {
        this.cartRepository = cartRepository;
        this.cartItemsRepository = cartItemsRepository;
        this.ordersRepository = ordersRepository;
        this.emailService = emailService;
    }

    /*
    Общая идея: добавляем lastUpdated колона Cart entity. Ето поле обновляеться при каждое добавление продукта
    Проверяем прошли ли больше 10 минут от момента now() и lastUpdated
    если ето да  то делаем  delete CartItems
    что бы ето работало в liquibase добавляем 005_data.sql
     */
    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES, initialDelay = 1)
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

    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.SECONDS, initialDelay = 30) // Runs every 30 seconds
    @Transactional
    public void updateOrderStatuses() {
        log.info("Running order status update task...");

        List<Orders> orders = ordersRepository.findAll(); // Retrieve all orders
        for (Orders order : orders) {
            OrdersStatus nextStatus = getNextStatus(order.getStatus());//здесь изпользовано цикличное изменение status
//            OrdersStatus nextStatus = goAllPaidForTestPurpuse(order.getStatus());//ето только с цель тест email sent to user!
            if (nextStatus != null) {
                order.setStatus(nextStatus);
                ordersRepository.save(order);
                log.info("Updated order {} status to {}", order.getId(), nextStatus);
            }
        }
    }
    /*
    Идея- все статусы меняются от CREATED->PENDING_PAYMENT->PAID->DELIVERED НЕ ЦИКЛИЧНОЕ!
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

    //ето только с цель тест email sent to user!
    private OrdersStatus goAllPaidForTestPurpuse(OrdersStatus currentStatus) {
        switch (currentStatus) {
            case CREATED:
                return PAID;
            case PENDING_PAYMENT:
                return OrdersStatus.PAID;
            case PAID:
                return PAID;
            case SHIPPED:
                return OrdersStatus.PAID;
            default:
                return PAID; // All Orders.staus->PAID
        }
    }
    /*
    Идея- все статусы меняются от CREATED->PENDING_PAYMENT->PAID->DELIVERED->CANCELED->CREATED ЦИКЛИЧНОЕ!
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

    /*
    идея:в ordersRepository имеем метод, которой ищет все orders в которой колона email_sent=False
    От всех подобных orders выбираем только те, у которых OrdersStatus.PAID. Ето List<Orders> paidOrders
    Запускаем в них цикл, меняем их колона email_sent=true и пользуем фабричний метод EmailSent
    конторой имееться в Maven to sent email to user
    Делаем ету проверку каждые 2 мин, с начальное опоздание 2 мин, что бы накопилис оплаченные Orders
    Внимание! Ето плохой подход! Надо изпользоват event listener в OrdersController которой следит за переход
    PENDING_PAYMENT->PAID. Здесь ето невозможно из за тригера. Пример рабочего listener
    @Transactional
    public Orders updateOrderStatus(Integer orderId, OrdersStatus newStatus) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        OrdersStatus oldStatus = order.getStatus();
        order.setStatus(newStatus);
        ordersRepository.save(order);
        // Send email only when status changes to PAID
        if (oldStatus != OrdersStatus.PAID && newStatus == OrdersStatus.PAID) {
            emailService.sendOrderConfirmation(order.getUsers().getEmail(), order);
        }
        return order;
    }
     */
    @Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES, initialDelay = 1) // Runs every 2 minutes//
    @Transactional
    public void sendPaymentConfirmationEmails() {
        log.info("Checking for orders with PAID status to send confirmation emails...");
        List<Orders> paidOrders = ordersRepository.findByStatusAndEmailSentFalse(OrdersStatus.PAID);
        for (Orders order : paidOrders) {
            try {
                emailService.sendOrderConfirmation(order.getUsers().getEmail(), order);
                order.setEmailSent(true);
                ordersRepository.save(order);
                log.info("Email sent for order {}", order.getId());
            } catch (Exception e) {
                log.error("Failed to send email for order {}", order.getId(), e);
            }
        }
    }
}


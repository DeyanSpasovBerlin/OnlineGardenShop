package finalproject.onlinegardenshop.scheduler;

import finalproject.onlinegardenshop.entity.Cart;
import finalproject.onlinegardenshop.entity.Orders;
import finalproject.onlinegardenshop.entity.enums.OrdersStatus;
import finalproject.onlinegardenshop.repository.CartItemsRepository;
import finalproject.onlinegardenshop.repository.CartRepository;
import finalproject.onlinegardenshop.repository.OrdersRepository;
import finalproject.onlinegardenshop.service.EmailService;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES, initialDelay = 1)
    @Transactional
    public void emptyCartAfterTenMinutes() {
        LocalDateTime now = LocalDateTime.now();
        List<Cart> carts = cartRepository.findAll();
        for (Cart cart : carts) {
            if (cart.getCartItems().isEmpty()) {
                continue;
            }
            if (cart.getLastUpdated() != null && ChronoUnit.MINUTES.between(cart.getLastUpdated(), now) < 10) {
                continue;
            }
            cartItemsRepository.deleteAll(cart.getCartItems());
            cart.getCartItems().clear();
            cartRepository.save(cart);
            logger.info("Cart items deleted successfully for cart ID: {}", cart.getId());
        }
    }

    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.SECONDS, initialDelay = 30)
    @Transactional
    public void updateOrderStatuses() {
        log.info("Running order status update task...");

        List<Orders> orders = ordersRepository.findAll();
        for (Orders order : orders) {
            OrdersStatus nextStatus = getNextStatus(order.getStatus());
//            OrdersStatus nextStatus = goAllPaidForTestPurpuse(order.getStatus());//only with purpuse test email sent to user!
            if (nextStatus != null) {
                order.setStatus(nextStatus);
                ordersRepository.save(order);
                log.info("Updated order {} status to {}", order.getId(), nextStatus);
            }
        }
    }

        //non-cyclic!
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
                    return null;
            }
        }

    //only with purpuse test email sent to user!
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
                return PAID;
        }
    }

    //cyclic!
    private OrdersStatus getNextStatus(OrdersStatus currentStatus) {
        OrdersStatus[] statuses = OrdersStatus.values();
        int currentIndex = currentStatus.ordinal();
        int nextIndex = (currentIndex + 1) % statuses.length;
        return statuses[nextIndex];
    }

    @Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES, initialDelay = 1)
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


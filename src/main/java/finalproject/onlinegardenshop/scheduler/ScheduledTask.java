package finalproject.onlinegardenshop.scheduler;

import finalproject.onlinegardenshop.entity.Cart;
import finalproject.onlinegardenshop.repository.CartItemsRepository;
import finalproject.onlinegardenshop.repository.CartRepository;
import finalproject.onlinegardenshop.service.OrdersService;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Log4j2
public class ScheduledTask {
    private static Logger logger = LogManager.getLogger(ScheduledTask.class);

    CartRepository  cartRepository;
    CartItemsRepository cartItemsRepository;

    @Autowired
    public ScheduledTask(CartRepository cartRepository, CartItemsRepository cartItemsRepository) {
        this.cartRepository = cartRepository;
        this.cartItemsRepository = cartItemsRepository;
    }

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES)
    @Transactional
    public void emptyCartAfterTenMinutes() {

        List<Cart> listCart = new ArrayList<>();
        listCart = cartRepository.findAll();
        if (!listCart.isEmpty()) {
            for (Cart c : listCart) {
                if (c.getId() == 2) {
                    cartItemsRepository.deleteAll(c.getCartItems());
                    c.getCartItems().clear();
                    cartRepository.save(c);
                    logger.info("Cart emptysuccessfully for user: {}", 2);
                }
            }
        }
    }
}


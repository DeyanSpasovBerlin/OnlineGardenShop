package finalproject.onlinegardenshop.runner;

import finalproject.onlinegardenshop.entity.Orders;
import finalproject.onlinegardenshop.service.EmailService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class EmailTestRunner implements CommandLineRunner {

    private final EmailService emailService;

    @Autowired
    public EmailTestRunner(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void run(String... args) {
        try {
            emailService.sendOrderConfirmation("onlinegardenshop.2025.berlin@gmail.com", new Orders());
            log.info("Test email sent successfully to onlinegardenshop.2025.berlin@gmail.com");
        } catch (Exception e) {
            log.error("Failed to send test email", e);
        }
    }
}

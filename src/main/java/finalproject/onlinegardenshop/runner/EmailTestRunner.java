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

    //when run app, this method start automaticalz and send test email to onlineGardenShop@gmail.com
    //to controm method public void sendPaymentConfirmationEmails() in public class ScheduledTask
    @Override
    public void run(String... args) {
        try {
            emailService.sendOrderConfirmation("onlinegardenshop.2025.berlin@gmail.com", new Orders());
            //onlinegardenshop.2025.berlin@gmail.com
            log.info("Test email sent successfully to onlinegardenshop.2025.berlin@gmail.com");
        } catch (Exception e) {
            log.error("Failed to send test email", e);
        }
    }
}

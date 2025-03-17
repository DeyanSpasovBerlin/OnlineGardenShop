package finalproject.onlinegardenshop.runner;

import finalproject.onlinegardenshop.entity.Orders;
import finalproject.onlinegardenshop.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
        emailService.sendOrderConfirmation("onlinegardenshop.2025.berlin@gmail.com", new Orders());//onlinegardenshop.2025.berlin@gmail.com
        System.out.println("Test email sent successfully!");
    }
}

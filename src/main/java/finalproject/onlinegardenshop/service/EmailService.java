package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.entity.Orders;
import finalproject.onlinegardenshop.exception.OnlineGardenShopBadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EmailService {
    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOrderConfirmation(String toEmail, Orders order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Order Confirmation");
        message.setText("Your order #" + order.getId() + " has been successfully paid!");
//        mailSender.send(message);
        try {
            mailSender.send(message);
        } catch (MailException e) {
            throw new OnlineGardenShopBadRequestException("Failed to send email: " + e.getMessage());
        }
    }

}

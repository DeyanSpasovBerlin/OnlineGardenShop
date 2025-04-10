package finalproject.onlinegardenshop.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import finalproject.onlinegardenshop.entity.OrderItems;
import finalproject.onlinegardenshop.entity.Orders;
import finalproject.onlinegardenshop.exception.OnlineGardenShopBadRequestException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.ByteArrayOutputStream;

@Service
@Transactional(readOnly = true)
public class EmailService {
    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOrderConfirmation(String toEmail, Orders order) {
        try {
            byte[] pdfContent = generateOrderPdf(order);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("noreply@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject("Order Confirmation");
            helper.setText("Your order #" + order.getId() + " has been successfully paid!");

            if (order.getUsers() != null) {
                helper.setText("Customer: " + order.getUsers().getFirstName() + " " + order.getUsers().getLastName());
            } else {
                helper.setText("Customer: No user data");
            }

            ByteArrayDataSource dataSource = new ByteArrayDataSource(pdfContent, "application/pdf");
            helper.addAttachment("order_" + order.getId() + ".pdf", dataSource);

            mailSender.send(message);
        } catch (MailException | jakarta.mail.MessagingException e) {
            throw new OnlineGardenShopBadRequestException("Failed to send email: " + e.getMessage());
        }
    }

    public byte[] generateOrderPdf(Orders order) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.RED);
            Paragraph title = new Paragraph("Order Details", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Order ID: " + order.getId()));

            document.add(new Paragraph("Customer: " +
                    (order.getUsers() != null ? order.getUsers().getFirstName() + " " + order.getUsers().getLastName() : "No user data")));

            document.add(new Paragraph("Delivery Address: " + order.getDeliveryAddress()));
            document.add(new Paragraph("Contact Phone: " + order.getContactPhone()));
            document.add(new Paragraph("Status: " + order.getStatus()));
            document.add(new Paragraph("Delivery Method: " + order.getDeliveryMethod()));

            Font priceFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLUE);
            document.add(new Paragraph("Total Price: €" + order.getTotalPrice(), priceFont));

            Font dateFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.GREEN);
            document.add(new Paragraph("Created At: " + order.getCreatedAt(), dateFont));
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.addCell("Product ID");
            table.addCell("Product Name");
            table.addCell("Quantity");
            table.addCell("Price at Purchase");

            for (OrderItems item : order.getOrderItems()) {
                table.addCell(String.valueOf(item.getProduct().getId()));
                table.addCell(item.getProduct().getName());
                table.addCell(String.valueOf(item.getQuantity()));

                PdfPCell priceCell = new PdfPCell(new Phrase(String.valueOf(item.getPriceAtPurchase()), priceFont));
                table.addCell(priceCell);
            }

            document.add(table);
            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new OnlineGardenShopBadRequestException("Failed to generate PDF: " + e.getMessage());
        }
    }
}

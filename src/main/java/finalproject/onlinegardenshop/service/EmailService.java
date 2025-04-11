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
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.DARK_GRAY);
            Paragraph title = new Paragraph("Order Details", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);
            Font regularFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
            document.add(new Paragraph("Order ID: " + order.getId(), regularFont));
            document.add(new Paragraph("Customer: " +
                    (order.getUsers() != null ? order.getUsers().getFirstName() + " " + order.getUsers().getLastName() : "No user data"), regularFont));
            document.add(new Paragraph("Delivery Address: " + order.getDeliveryAddress(), regularFont));
            document.add(new Paragraph("Contact Phone: " + order.getContactPhone(), regularFont));
            document.add(new Paragraph("Status: " + order.getStatus(), regularFont));
            document.add(new Paragraph("Delivery Method: " + order.getDeliveryMethod(), regularFont));
            document.add(new Paragraph("Total Price: €" + order.getTotalPrice(), regularFont));
            document.add(new Paragraph("Created At: " + order.getCreatedAt(), regularFont));
            document.add(Chunk.NEWLINE);
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10); // Padding преди таблицата
            Font tableHeaderFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
            PdfPCell headerCell = new PdfPCell(new Phrase("Product ID", tableHeaderFont));
            headerCell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(headerCell);
            headerCell = new PdfPCell(new Phrase("Product Name", tableHeaderFont));
            headerCell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(headerCell);
            headerCell = new PdfPCell(new Phrase("Quantity", tableHeaderFont));
            headerCell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(headerCell);
            headerCell = new PdfPCell(new Phrase("Price at Purchase", tableHeaderFont));
            headerCell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(headerCell);
            Font tableCellFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
            for (OrderItems item : order.getOrderItems()) {
                table.addCell(new PdfPCell(new Phrase(String.valueOf(item.getProduct().getId()), tableCellFont)));
                table.addCell(new PdfPCell(new Phrase(item.getProduct().getName(), tableCellFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(item.getQuantity()), tableCellFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(item.getPriceAtPurchase()), tableCellFont)));
            }
            document.add(table);
            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new OnlineGardenShopBadRequestException("Failed to generate PDF: " + e.getMessage());
        }
    }
}
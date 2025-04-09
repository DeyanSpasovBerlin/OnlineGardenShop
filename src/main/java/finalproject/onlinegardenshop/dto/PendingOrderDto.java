package finalproject.onlinegardenshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PendingOrderDto {
    private Integer orderId;
    private String productName;
    private LocalDateTime orderDate;
}
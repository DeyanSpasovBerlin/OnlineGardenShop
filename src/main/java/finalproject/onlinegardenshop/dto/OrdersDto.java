package finalproject.onlinegardenshop.dto;

import finalproject.onlinegardenshop.entity.enums.DeliveryMethod;
import finalproject.onlinegardenshop.entity.enums.OrdersStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrdersDto {

    private Integer id;

    @NotNull(message="{validation.Orders.deliveryAddress}")
    @Pattern(regexp = "^[A-Za-zäöüÄÖÜß0-9\\s.,-]{5,150}$",message = "{validation.Orders.deliveryAddress}")
    @Length(max = 150, message ="{validation.Orders.deliveryAddress}")
    private String deliveryAddress;

    @NotNull(message="{validation.Orders.contactPhone}")
    @Pattern(regexp = "^\\+49\\s?[1-9][0-9]{1,4}[-\\s]?[0-9]{3,12}$",message = "{validation.Orders.contactPhone}")
    private String contactPhone;

    @Enumerated(EnumType.STRING)
    private OrdersStatus status;

    @Enumerated(EnumType.STRING)
    private DeliveryMethod deliveryMethod;

    private Integer usersId;
}

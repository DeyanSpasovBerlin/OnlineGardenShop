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

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrdersDto {

    private Integer id;

    private Integer deletedUserId;

    @NotNull(message="{validation.Orders.deliveryAddress}")
    @Pattern(regexp = "^[A-Za-zäöüÄÖÜß0-9\\s.,-]{5,150}$",message = "{validation.orders.deliveryAddress}")
    @Length(max = 150, message ="{validation.orders.deliveryAddress}")
    private String deliveryAddress;

    @NotNull(message="{validation.Orders.contactPhone}")
    @Pattern(regexp ="^\\+49\\s?[1-9][0-9]{1,4}([\\s-]?[0-9]{2,12})*$" ,message = "{validation.orders.contactPhone}")//"^\\+49\\s?[1-9][0-9]{1,4}[-\\s]?[0-9]{3,12}$"
    private String contactPhone;

    //overide set wit aim to cut spaces after phone number
    public void setContactPhone(String contactPhone) {
        if (contactPhone != null) {
            this.contactPhone = contactPhone.replaceAll("[\\s-]", "");
        }
    }

    @Enumerated(EnumType.STRING)
    private OrdersStatus status;

    @Enumerated(EnumType.STRING)
    private DeliveryMethod deliveryMethod;

    private boolean emailSent;//this show if email is sent to user

    private Integer usersId;

    private List<CreateOrderRequestSaveOrderItemsDto> items;//ето позволяет когда создаю order  записать  и создать orderItems


}

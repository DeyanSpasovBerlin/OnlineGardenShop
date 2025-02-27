package finalproject.onlinegardenshop.entity;

import finalproject.onlinegardenshop.entity.enums.DeliveryMethod;
import finalproject.onlinegardenshop.entity.enums.OrdersStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "int")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "int")
    private Users user;

    private String deliveryAdress;

    private String contactPhone;

    @Enumerated(EnumType.STRING)
    private OrdersStatus status;

    @Enumerated(EnumType.STRING)
    private DeliveryMethod deliveryMethod;


}

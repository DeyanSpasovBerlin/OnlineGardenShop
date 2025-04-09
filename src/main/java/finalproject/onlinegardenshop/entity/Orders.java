package finalproject.onlinegardenshop.entity;

import finalproject.onlinegardenshop.entity.enums.DeliveryMethod;
import finalproject.onlinegardenshop.entity.enums.OrdersStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "int")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", columnDefinition = "int")
    private Users users;

    @Column(name = "deleted_user_id", nullable = true)
    private Integer deletedUserId;

    private String deliveryAddress;

    private String contactPhone;

    @Enumerated(EnumType.STRING)
    private OrdersStatus status;

    @Enumerated(EnumType.STRING)
    private DeliveryMethod deliveryMethod;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItems> orderItems = new ArrayList<>();

    private Double totalPrice;

    @Column(name = "email_sent", nullable = false)
    private boolean emailSent = false;

}

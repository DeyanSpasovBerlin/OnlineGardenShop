package finalproject.onlinegardenshop.entity;

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
@Table(name = "cart_items")
public class CartItems {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;

    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY) // Many items can belong to one cart
    @JoinColumn(name = "cart_id", nullable = false) // Foreign Key
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY) // Many items can refer to one product
    @JoinColumn(name = "product_id", nullable = false) // Foreign Key
    private Products products;
}

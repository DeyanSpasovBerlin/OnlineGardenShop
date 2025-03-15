package finalproject.onlinegardenshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "int")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", columnDefinition = "int", nullable = true)//-> , nullable = true- alow delete.user
    private Users users;

    @OneToMany(mappedBy = "cart", cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Add CascadeType.ALL here
    private List<CartItems> cartItems;

//    @Column(name = "completed", nullable = false)// add to cart table!ето для более научное отношение к Cart
    //мы не только del CartItems, но и ставим falg completed на Cart, что бы указать, что ее содержание через
    //checkout(Integer userId) записано в Orders. Более простой вариант которой я сделал CartItems del-> Cart empty
//    private boolean completed = false;

    //Ето нам нужно что бы можно в Scheduled отсчитать 10 минут от посленего добавления CartItems
    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    // Automatically update 'lastUpdated' before persisting or updating
    @PreUpdate// Runs before the entity is updated (modified in the database).
    @PrePersist// Runs before the entity is first saved (inserted into the database).
    protected void onUpdate() {
        this.lastUpdated = LocalDateTime.now();//automatically set to LocalDateTime.now().
    }


}

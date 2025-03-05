package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemsRepository extends JpaRepository<CartItems,Integer> {

}

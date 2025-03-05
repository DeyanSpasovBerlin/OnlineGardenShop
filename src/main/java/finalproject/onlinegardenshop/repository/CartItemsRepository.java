package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemsRepository extends JpaRepository<CartItems,Integer> {
//    Optional<CartItems> findById(Integer id);
}

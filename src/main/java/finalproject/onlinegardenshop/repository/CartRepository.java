package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Integer> {

    Optional<Cart> findById(Integer id);

    Optional<Cart> findByUsersId(Integer userId);

//    Optional<Cart> findByUsersIdAndCompletedFalse(Integer userId);//ето если будем флаг исрользоват! completed

    //Отбираем только те Cart у которых имеються CartItems. Ето нужно для Scheduled public void emptyCartAfterTenMinutes()
//    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.cartItems")
//    List<Cart> findAllWithCartItems();
}

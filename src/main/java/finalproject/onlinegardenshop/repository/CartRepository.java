package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Integer> {

    Optional<Cart> findById(Integer id);

    Optional<Cart> findByUsersId(Integer userId);

//    Optional<Cart> findByUsersIdAndCompletedFalse(Integer userId);//ето если будем флаг исрользоват! completed
}

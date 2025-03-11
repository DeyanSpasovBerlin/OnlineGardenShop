package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.entity.Cart;
import finalproject.onlinegardenshop.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders,Integer> {

    List<Orders> findByUsersId(Integer userId);

}

package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.entity.Cart;
import finalproject.onlinegardenshop.entity.Orders;
import finalproject.onlinegardenshop.entity.enums.OrdersStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders,Integer> {

    List<Orders> findByUsersId(Integer userId);

    @Query("update Orders o set o.status = :status where o.id = :id")
    @Modifying
    int updateStatus(Integer id, OrdersStatus status);

}

package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.entity.Orders;
import finalproject.onlinegardenshop.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders,Integer> {

}

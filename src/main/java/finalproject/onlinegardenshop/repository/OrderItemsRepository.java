package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItem, Integer> {
}

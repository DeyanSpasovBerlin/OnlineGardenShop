package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Integer> {
}

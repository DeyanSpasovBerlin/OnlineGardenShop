package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Integer> {

    //Добавлено в целях метода удаления продуктов в ProductsService
    @Modifying
    @Transactional
    @Query("UPDATE OrderItems oi SET oi.product = NULL WHERE oi.product.id = :id")
    void updateProductToNull(@Param("id") Integer id);


}

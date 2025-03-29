package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.dto.RevenueReportDto;
import finalproject.onlinegardenshop.dto.TopCanceledProductDto;
import finalproject.onlinegardenshop.dto.TopSoldProductDto;
import finalproject.onlinegardenshop.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Integer> {

    //Добавлено в целях метода удаления продуктов в ProductsService
    @Modifying
    @Transactional
    @Query("UPDATE OrderItems oi SET oi.product = NULL WHERE oi.product.id = :id")
    void updateProductToNull(@Param("id") Integer id);

    //we need this in TopSoldProduc  Топ 10 най-продавани продукти
    @Query("SELECT new finalproject.onlinegardenshop.dto.TopSoldProductDto(oi.product.name, SUM(oi.quantity)) " +
            "FROM OrderItems oi GROUP BY oi.product.id " +
            "ORDER BY SUM(oi.quantity) DESC LIMIT 10")
    List<TopSoldProductDto> findTop10SoldProducts();

    //we need this in opCanceledProduct  Топ 10 най-отменяни продукти
    @Query("SELECT new finalproject.onlinegardenshop.dto.TopCanceledProductDto(oi.product.name, COUNT(oi.id)) " +
            "FROM OrderItems oi JOIN oi.order o " +
            "WHERE o.status = 'CANCELED' " +
            "GROUP BY oi.product.id " +
            "ORDER BY COUNT(oi.id) DESC LIMIT 10")
    List<TopCanceledProductDto> findTop10CanceledProducts();



}

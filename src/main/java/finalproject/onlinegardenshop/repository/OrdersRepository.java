package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.dto.PendingOrderDto;
import finalproject.onlinegardenshop.dto.RevenueReportDto;
import finalproject.onlinegardenshop.entity.Cart;
import finalproject.onlinegardenshop.entity.Orders;
import finalproject.onlinegardenshop.entity.enums.OrdersStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders,Integer> {

    List<Orders> findByUsersId(Integer userId);

    @Query("update Orders o set o.status = :status where o.id = :id")
    @Modifying
    int updateStatus(Integer id, OrdersStatus status);

    // Find orders for a specific deleted user
    List<Orders> findByDeletedUserId(Integer deletedUserId);

    // Find all orders that belong to deleted users
    List<Orders> findAllByDeletedUserIdIsNotNull();

    //Find all Orders which sendMail=false
    List<Orders> findByStatusAndEmailSentFalse(OrdersStatus status);

    //we need this in PendingOrder  Неплатени поръчки повече от N дни
    @Query("SELECT new finalproject.onlinegardenshop.dto.PendingOrderDto(o.id, oi.product.name, o.createdAt) " +
            "FROM Orders o JOIN o.orderItems oi " +
            "WHERE o.status = 'PENDING_PAYMENT' AND o.createdAt < :thresholdDate")
    List<PendingOrderDto> findPendingOrdersOlderThan(@Param("thresholdDate") LocalDateTime thresholdDate);

    //we need this in RevenueReport  Приходи за период изчисляваме датата в OrdersService
//
//    @Query("SELECT new finalproject.onlinegardenshop.dto.RevenueReportDto( " +
//            "CASE " +
//            "WHEN :period = 'hour' THEN FUNCTION('DATE_FORMAT', o.createdAt, '%Y-%m-%d %H:00:00') " +
//            "WHEN :period = 'day' THEN FUNCTION('DATE_FORMAT', o.createdAt, '%Y-%m-%d') " +
//            "WHEN :period = 'week' THEN FUNCTION('DATE_FORMAT', o.createdAt, '%Y-%u') " +
//            "WHEN :period = 'month' THEN FUNCTION('DATE_FORMAT', o.createdAt, '%Y-%m') " +
//            "END, SUM(o.totalPrice)) " +
//            "FROM Orders o " +
//            "WHERE o.createdAt >= :startDate " +
//            "GROUP BY " +
//            "CASE " +
//            "WHEN :period = 'hour' THEN FUNCTION('DATE_FORMAT', o.createdAt, '%Y-%m-%d %H:00:00') " +
//            "WHEN :period = 'day' THEN FUNCTION('DATE_FORMAT', o.createdAt, '%Y-%m-%d') " +
//            "WHEN :period = 'week' THEN FUNCTION('DATE_FORMAT', o.createdAt, '%Y-%u') " +
//            "WHEN :period = 'month' THEN FUNCTION('DATE_FORMAT', o.createdAt, '%Y-%m') " +
//            "END " +
//            "ORDER BY 1 DESC")
//    List<RevenueReportDto> findRevenueGroupedByPeriod(
//            @Param("period") String period,
//            @Param("startDate") LocalDateTime startDate
//    );
    //******************

//    @Query("SELECT new finalproject.onlinegardenshop.dto.RevenueReportDto(" +
//            "DATE(o.createdAt), SUM(o.totalPrice)) " +
//            "FROM Orders o " +
//            "GROUP BY DATE(o.createdAt) " +
//            "ORDER BY DATE(o.createdAt)")
//    List<RevenueReportDto> getRevenueForLast10Days();
    @Query(value = "SELECT DATE(created_at) AS period, SUM(total_price) AS totalRevenue " +
            "FROM orders " +
            "GROUP BY DATE(created_at) " +
            "ORDER BY period", nativeQuery = true)
    List<Object[]> getRevenueForLast10Days();
//    //********************
//    @Query(value = "SELECT COUNT(*) FROM orders", nativeQuery = true)
//    Long countOrders();
    //***********

}

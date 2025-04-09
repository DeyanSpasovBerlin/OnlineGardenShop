package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.dto.PendingOrderDto;
import finalproject.onlinegardenshop.dto.RevenueReportDto;
import finalproject.onlinegardenshop.entity.Orders;
import finalproject.onlinegardenshop.entity.enums.OrdersStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders,Integer> {

    List<Orders> findByUsersId(Integer userId);

    @Query("update Orders o set o.status = :status where o.id = :id")
    @Modifying
    int updateStatus(Integer id, OrdersStatus status);

    List<Orders> findByDeletedUserId(Integer deletedUserId);

    List<Orders> findAllByDeletedUserIdIsNotNull();

    List<Orders> findByStatusAndEmailSentFalse(OrdersStatus status);

    @Query("SELECT new finalproject.onlinegardenshop.dto.PendingOrderDto(o.id, oi.product.name, o.createdAt) " +
            "FROM Orders o JOIN o.orderItems oi " +
            "WHERE o.status = 'PENDING_PAYMENT' AND o.createdAt < :thresholdDate")
    List<PendingOrderDto> findPendingOrdersOlderThan(@Param("thresholdDate") LocalDateTime thresholdDate);

    @Query(value = "SELECT CASE " +
            "   WHEN :intervalType = 'HOUR' THEN DATE_FORMAT(o.created_at, '%Y-%m-%d %H:00:00') " +
            "   WHEN :intervalType = 'DAY' THEN DATE_FORMAT(o.created_at, '%Y-%m-%d') " +
            "   WHEN :intervalType = 'WEEK' THEN CONCAT(YEAR(o.created_at), '-W', WEEK(o.created_at)) " +
            "   WHEN :intervalType = 'MONTH' THEN DATE_FORMAT(o.created_at, '%Y-%m') " +
            "   WHEN :intervalType = 'YEAR' THEN YEAR(o.created_at) " +
            "END AS period, " +
            "SUM(oi.quantity * oi.price_at_purchase) AS total_revenue " +
            "FROM orders o " +
            "LEFT JOIN order_items oi ON o.id = oi.orders_id " +
            "WHERE o.created_at >= DATE_SUB(NOW(), INTERVAL :n DAY) " +
            "GROUP BY period " +
            "ORDER BY period", nativeQuery = true)
    List<Object[]> getRevenueReport(@Param("n") int n, @Param("intervalType") String intervalType);

    default List<RevenueReportDto> getRevenueReportDto(int n, String intervalType) {
        List<Object[]> results = getRevenueReport(n, intervalType);
        List<RevenueReportDto> reportDtos = new ArrayList<>();
        for (Object[] result : results) {
            String period = (String) result[0];
            Double totalRevenue = (Double) result[1];
            reportDtos.add(new RevenueReportDto(period, totalRevenue));
        }
        return reportDtos;
    }

}

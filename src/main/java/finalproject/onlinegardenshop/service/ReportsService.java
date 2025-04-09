package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.PendingOrderDto;
import finalproject.onlinegardenshop.dto.RevenueReportDto;
import finalproject.onlinegardenshop.dto.TopCanceledProductDto;
import finalproject.onlinegardenshop.dto.TopSoldProductDto;
import finalproject.onlinegardenshop.mapper.OrdersMapper;
import finalproject.onlinegardenshop.repository.OrderItemsRepository;
import finalproject.onlinegardenshop.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReportsService {
    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final OrdersMapper ordersMapper;

    @Autowired
    public ReportsService(OrdersRepository ordersRepository, OrderItemsRepository orderItemsRepository, OrdersMapper ordersMapper) {
        this.ordersRepository = ordersRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.ordersMapper = ordersMapper;
    }

    public List<TopSoldProductDto> getTop10SoldProducts() {
        return orderItemsRepository.findTop10SoldProducts();
    }

    public List<TopCanceledProductDto> getTop10CanceledProducts() {
        return orderItemsRepository.findTop10CanceledProducts();
    }

    public List<PendingOrderDto> getPendingOrdersOlderThan(int days) {
        LocalDateTime thresholdDate = LocalDateTime.now().minusDays(days);
        return ordersRepository.findPendingOrdersOlderThan(thresholdDate);
    }

    public List<RevenueReportDto> getRevenueReport(int n, String intervalType) {
        return ordersRepository.getRevenueReportDto(n, intervalType);
    }

}

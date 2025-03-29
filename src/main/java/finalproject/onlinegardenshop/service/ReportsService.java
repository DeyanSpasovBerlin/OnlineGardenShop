package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.PendingOrderDto;
import finalproject.onlinegardenshop.dto.RevenueReportDto;
import finalproject.onlinegardenshop.dto.TopCanceledProductDto;
import finalproject.onlinegardenshop.dto.TopSoldProductDto;
import finalproject.onlinegardenshop.mapper.OrdersMapper;
import finalproject.onlinegardenshop.mapper.ReportsMapper;
import finalproject.onlinegardenshop.repository.OrderItemsRepository;
import finalproject.onlinegardenshop.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReportsService {
    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final OrdersMapper ordersMapper;
    private  final ReportsMapper reportsMapper;

    @Autowired
    public ReportsService(OrdersRepository ordersRepository, OrderItemsRepository orderItemsRepository, OrdersMapper ordersMapper, ReportsMapper reportsMapper) {
        this.ordersRepository = ordersRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.ordersMapper = ordersMapper;
        this.reportsMapper = reportsMapper;
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


//    public List<RevenueReportDto> getRevenueForLastPeriod(int count, String period) {
//        LocalDateTime startDate = calculateStartDate(count, period);
//        return ordersRepository.findRevenueGroupedByPeriod(period, startDate);
//    }
//    public List<RevenueReportDto> getRevenueReport(String period, LocalDateTime startDate) {
//        return ordersRepository.findRevenueGroupedByPeriod(period, startDate);
//    }
//Пресмятане на началната дата
//    private LocalDateTime calculateStartDate(int count, String period) {
//        switch (period.toLowerCase()) {
//            case "hour":
//                return LocalDateTime.now().minusHours(count);
//            case "day":
//                return LocalDateTime.now().minusDays(count);
//            case "week":
//                return LocalDateTime.now().minusWeeks(count);
//            case "month":
//                return LocalDateTime.now().minusMonths(count);
//            default:
//                throw new IllegalArgumentException("Invalid period: " + period);
//        }
//    }
    //******************
//    public List<RevenueReportDto> getRevenueForLast10Days() {
//        List<Object[]> results = ordersRepository.getRevenueForLast10Days();
//        List<RevenueReportDto> revenueReports = new ArrayList<>();
//
//        for (Object[] result : results) {
//            LocalDateTime period = (LocalDateTime) result[0];  // Преобразуване на дата
//            Double totalRevenue = (Double) result[1];  // Преобразуване на сума
//
//            revenueReports.add(new RevenueReportDto(period, totalRevenue));  // Добавяне към DTO
//        }
//
//        return revenueReports;
//    }

//    public List<RevenueReportDto> getRevenueForLast10Days() {
//        List<Object[]> results = ordersRepository.getRevenueForLast10Days();
//        List<RevenueReportDto> revenueReports = new ArrayList<>();
//
//        if (results.isEmpty()) {
//            System.out.println("No data found.");
//        } else {
//            for (Object[] result : results) {
//                LocalDateTime period = (LocalDateTime) result[0];  // Период (Дата)
//                Double totalRevenue = (Double) result[1];  // Обща сума на поръчките за деня
//                revenueReports.add(new RevenueReportDto(period, totalRevenue));
//            }
//        }
//
//        return revenueReports;
//    }
    public List<RevenueReportDto> getRevenueForLast10Days() {
        List<Object[]> results = ordersRepository.getRevenueForLast10Days();
//        return ordersMapper.mapToRevenueReportDtoList(results);  // Извикваме метода за ръчно преобразуване
        List<RevenueReportDto> listRevenueReportDto = new ArrayList<>();
        for (Object[] o:results){
            listRevenueReportDto.add(convertToRevenueReportDto(o));
        }
        return listRevenueReportDto;
    }

    public RevenueReportDto convertToRevenueReportDto(Object[] row) {
        LocalDateTime period = (LocalDateTime) row[0];
        Double totalRevenue = (Double) row[1];

        return new RevenueReportDto(period, totalRevenue);
    }
    //*********************
//    public Long getOrderCount() {
//        return ordersRepository.countOrders();
//    }
    //*************
}

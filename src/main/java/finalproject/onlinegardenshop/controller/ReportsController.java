package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.PendingOrderDto;
import finalproject.onlinegardenshop.dto.RevenueReportDto;
import finalproject.onlinegardenshop.dto.TopCanceledProductDto;
import finalproject.onlinegardenshop.dto.TopSoldProductDto;
import finalproject.onlinegardenshop.service.ReportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportsController {
    private final ReportsService reportsService;

    @Autowired
    public ReportsController(ReportsService reportsService) {
        this.reportsService = reportsService;
    }

    @GetMapping("/top-sold")
    public List<TopSoldProductDto> getTop10SoldProducts() {
        return reportsService.getTop10SoldProducts();
    }
    //GET http://localhost:8080/reports/top-sold  Топ 10 най-продавани продукти

    @GetMapping("/top-canceled")
    public List<TopCanceledProductDto> getTop10CanceledProducts() {
        return reportsService.getTop10CanceledProducts();
    }
    //GET http://localhost:8080/reports/top-canceled  Топ 10 най-отменяни продукти

    @GetMapping("/pending-orders/{days}")
    public List<PendingOrderDto> getPendingOrdersOlderThan(@PathVariable int days) {
        return reportsService.getPendingOrdersOlderThan(days);
    }
    //GET http://localhost:8080/reports/pending-orders/7  Неплатени поръчки повече от 7 дни

//    @GetMapping("/revenue")
//    public List<RevenueReportDto> getRevenueForLastPeriod(
//            @RequestParam int count,
//            @RequestParam String period
//    ) {
//        return reportsService.getRevenueForLastPeriod(count, period);
//    }
//    orders/revenue?count=7&period=day → приходи за последните 7 дни
//    orders/revenue?count=3&period=month → приходи за последните 3 месеца

//    @GetMapping("/revenue")
//    public List<RevenueReportDto> getRevenueReport(@RequestParam String period, @RequestParam LocalDateTime startDate) {
//        return reportsService.getRevenueReport(period, startDate);
//    }
    //*****************

//    @GetMapping("/revenue/last-10-days")
//    public List<RevenueReportDto> getRevenueForLast10Days() {
//        return reportsService.getRevenueForLast10Days();
//    }
//    @GetMapping("/revenue/last-10-days")
//    public ResponseEntity<List<RevenueReportDto>> getRevenueForLast10Days() {
//        try {
//            List<RevenueReportDto> revenueReports = reportsService.getRevenueForLast10Days();
//            if (revenueReports.isEmpty()) {
//                return ResponseEntity.noContent().build();  // Ако няма данни
//            }
//            return ResponseEntity.ok(revenueReports);
//        } catch (Exception e) {
//            // Логиране на грешката
//            System.err.println("Error fetching revenue data: " + e.getMessage());
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // Връща 500 при грешка
//        }
//    }
    @GetMapping("/revenue/last-10-days")
    public ResponseEntity<List<RevenueReportDto>> getRevenueForLast10Days() {
        List<RevenueReportDto> revenueReportDtos = reportsService.getRevenueForLast10Days();
        return ResponseEntity.ok(revenueReportDtos);
    }
    //GET http://localhost:8080/reports/revenue/last-10-days
    //*********************
//    @GetMapping("/count-orders")
//    public ResponseEntity<Long> getOrderCount() {
//        try {
//            Long orderCount = reportsService.getOrderCount();
//            return ResponseEntity.ok(orderCount);  // Връща брой на поръчките
//        } catch (Exception e) {
//            // Логиране на грешката
//            System.err.println("Error fetching order count: " + e.getMessage());
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // Връща 500 при грешка
//        }
//    }
    //****************
}
package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.PendingOrderDto;
import finalproject.onlinegardenshop.dto.RevenueReportDto;
import finalproject.onlinegardenshop.dto.TopCanceledProductDto;
import finalproject.onlinegardenshop.dto.TopSoldProductDto;
import finalproject.onlinegardenshop.service.ReportsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reports")
@Tag(name = "Reports Controller", description = "REST API to manage product statistics in the app")
public class ReportsController {
    private final ReportsService reportsService;

    @Autowired
    public ReportsController(ReportsService reportsService) {
        this.reportsService = reportsService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/top-sold")
    @Operation(summary = "Returns a list of the top 10 best selling products")
    public List<TopSoldProductDto> getTop10SoldProducts() {
        return reportsService.getTop10SoldProducts();
    }
    //GET http://localhost:8080/reports/top-sold  Топ 10 най-продавани продукти

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/top-canceled")
    @Operation(summary = "Returns a list of 10 most cancelled products")
    public List<TopCanceledProductDto> getTop10CanceledProducts() {
        return reportsService.getTop10CanceledProducts();
    }
    //GET http://localhost:8080/reports/top-canceled  Топ 10 най-отменяни продукти

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/pending-orders/{days}")
    @Operation(summary = "Returns a list of the orders pending payment over a certain period (in days)")
    public List<PendingOrderDto> getPendingOrdersOlderThan(@PathVariable int days) {
        return reportsService.getPendingOrdersOlderThan(days);
    }
    //GET http://localhost:8080/reports/pending-orders/7  Неплатени поръчки повече от 7 дни

    // Метод за генериране на отчет за приходи по дни, месеци, часове и т.н.
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/revenue")
    @Operation(summary = "Returns a revenue report for a certain period")
    public List<RevenueReportDto> getRevenueReport(
            @RequestParam("n") int n,                    // Параметър за броя дни/месеци/години
            @RequestParam("intervalType") String intervalType) {  // Тип на интервала (например 'DAY', 'HOUR', 'MONTH')
        return reportsService.getRevenueReport(n, intervalType);
    }
}
/*
1. Заявка за групиране по час (HOUR)
Тази заявка ще върне приходи, групирани по час.
    http://localhost:8080/reports/revenue?intervalType=HOUR&n=30
    Описание:Тази заявка ще върне общите приходи за последните 30 часа.
    ******************
    2. Заявка за групиране по ден (DAY)
Тази заявка ще върне приходи, групирани по ден.
    http://localhost:8080/reports/revenue?intervalType=DAY&n=30
    Описание: Тази заявка ще върне общите приходи за последните 30 дни.
    ********************
    3. Заявка за групиране по седмица (WEEK)
Тази заявка ще върне приходи, групирани по седмица.
    http://localhost:8080/reports/revenue?intervalType=WEEK&n=12
    Описание: Тази заявка ще върне общите приходи за последните 12 седмици.
    ******************
    5. Заявка за групиране по година (YEAR)
Тази заявка ще върне приходи, групирани по година.
    http://localhost:8080/reports/revenue?intervalType=YEAR&n=5
    Описание: Тази заявка ще върне общите приходи за последните 5 години.
 */
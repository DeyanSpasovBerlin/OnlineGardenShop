package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.PendingOrderDto;
import finalproject.onlinegardenshop.dto.RevenueReportDto;
import finalproject.onlinegardenshop.dto.TopCanceledProductDto;
import finalproject.onlinegardenshop.dto.TopSoldProductDto;
import finalproject.onlinegardenshop.service.ReportsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/top-canceled")
    @Operation(summary = "Returns a list of 10 most cancelled products")
    public List<TopCanceledProductDto> getTop10CanceledProducts() {
        return reportsService.getTop10CanceledProducts();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/pending-orders/{days}")
    @Operation(summary = "Returns a list of the orders pending payment over a certain period (in days)")
    public List<PendingOrderDto> getPendingOrdersOlderThan(@PathVariable int days) {
        return reportsService.getPendingOrdersOlderThan(days);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/revenue")
    @Operation(summary = "Returns a revenue report for a certain period")
    public List<RevenueReportDto> getRevenueReport(
            @RequestParam("n") int n,
            @RequestParam("intervalType") String intervalType) {
        return reportsService.getRevenueReport(n, intervalType);
    }
}

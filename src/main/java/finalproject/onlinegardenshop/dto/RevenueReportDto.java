package finalproject.onlinegardenshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RevenueReportDto {
    private LocalDateTime period; // Период (например, дата, час, седмица и т.н.)
    private Double totalRevenue; // Обща печалба за този период

}
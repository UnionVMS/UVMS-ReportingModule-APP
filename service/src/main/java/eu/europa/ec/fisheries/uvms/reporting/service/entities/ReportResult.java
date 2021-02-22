package eu.europa.ec.fisheries.uvms.reporting.service.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResult {

    @Override
    public String toString() {
        return "ReportResult{" +
                "activityReportResult=" + activityReportResult +
                ", movementReportResult=" + movementReportResult +
                ", pageNumber=" + pageNumber +
                ", resultsPerPage=" + resultsPerPage +
                ", totalElements=" + totalElements +
                ", totalPages=" + totalPages +
                '}';
    }

    List<ActivityReportResult> activityReportResult;
    List<MovementReportResult> movementReportResult;
    Integer pageNumber;
    Integer resultsPerPage;
    Integer totalElements;
    Integer totalPages;

}

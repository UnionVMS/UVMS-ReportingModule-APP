package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.DateTimeFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;

public class ObjectFactory {

    public Report createReport() {
        return Report.ReportBuilder().build();
    }

    public DateTimeFilter createPositionFilter() {
        return DateTimeFilter.DateTimeFilterBuilder().build();
    }

    public ReportDTO createReportDTO() {
        return ReportDTO.builder().build();
    }

}

package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.PositionFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;

public class ObjectFactory {

    public Report createReport() {
        return Report.builder().build();
    }

    public PositionFilter createPositionFilter() {
        return PositionFilter.builder().build();
    }

    public ReportDTO createReportDTO() {
        return ReportDTO.builder().build();
    }

}

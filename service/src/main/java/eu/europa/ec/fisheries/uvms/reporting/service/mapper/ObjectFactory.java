package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.CommonFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.PositionSelector;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;

public class ObjectFactory {

    public Report createReport() {
        return Report.ReportBuilder().build();
    }

    public CommonFilter createPositionFilter() {
        return CommonFilter.DateTimeFilterBuilder().build();
    }

    public ReportDTO createReportDTO() {
        return ReportDTO.builder().build();
    }

    public PositionSelector createPositionSelector() {
        return PositionSelector.PositionSelectorBuilder().build();
    }

}

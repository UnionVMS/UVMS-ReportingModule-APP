package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import java.util.List;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.DisplayFormat;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ExecutionResultDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ActivityReportResult;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.MovementReportResult;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import org.joda.time.DateTime;

public interface ReportingDataService {

    List<MovementReportResult> executeMovementReport(Report report, DateTime now, List<AreaIdentifierType> areaRestrictions, Boolean withActivity, DisplayFormat displayFormat, Long pageNumber, Long pageSize);

    List<ActivityReportResult> findActivityReportByReportId(final Report report, int firstResult, int maxResults);
}

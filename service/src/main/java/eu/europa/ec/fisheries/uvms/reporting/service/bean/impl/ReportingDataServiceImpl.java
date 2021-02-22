package eu.europa.ec.fisheries.uvms.reporting.service.bean.impl;

import javax.inject.Inject;
import java.util.List;

import eu.europa.ec.fisheries.uvms.reporting.service.bean.MovementRepository;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.ReportingDataService;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.DisplayFormat;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ExecutionResultDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import org.joda.time.DateTime;

public class ReportingDataServiceImpl implements ReportingDataService {

    @Inject
    private MovementRepository movementRepository;

    @Override
    public ExecutionResultDTO executeReport(Report report, DateTime now,
                                            List<AreaIdentifierType> areaRestrictions,
                                            Boolean withActivity,
                                            DisplayFormat displayFormat,
                                            Long pageNumber,
                                            Long pageSize) {

        long offset = pageNumber != null ? pageNumber * pageSize : 0;
        long limit = pageSize != null ? pageSize : 1000;

        StringBuilder query =
                new StringBuilder()
                        .append("SELECT m.movement_guid ")
                        .append("FROM reporting.movement m ")
                        .append("JOIN reporting.segment s ON s.movement_guid = m.movement_guid ")
                        .append("JOIN reporting.track t ON t.id = s.track_id ")
                        .append("JOIN reporting.asset a ON a.asset_hist_guid = m.connect_id ");

//        report.getFilters().forEach(f -> {
//
//            switch (f.getType()) {
//                case FilterType.vmstrack:
//                    query.
//                    break;
//                default:
//            }
//
//        });

        query.append("ORDER BY m.id DESC ");
        query.append("OFFSET " + offset + " LIMIT " + limit);

        movementRepository.executeQuery(query.toString());
        return new ExecutionResultDTO();
    }
}

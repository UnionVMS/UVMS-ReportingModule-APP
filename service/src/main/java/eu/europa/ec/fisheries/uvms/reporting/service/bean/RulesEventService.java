package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.schema.rules.ticketrule.v1.TicketAndRuleType;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;

import java.util.List;

/**
 * Created by padhyad on 3/23/2016.
 */
public interface RulesEventService {

    List<TicketAndRuleType> GetAlarmsForMovements(List<String> movementId) throws ReportingServiceException;
}

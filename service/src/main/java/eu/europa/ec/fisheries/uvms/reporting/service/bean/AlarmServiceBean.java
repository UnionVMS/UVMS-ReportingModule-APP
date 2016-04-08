package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.schema.rules.ticketrule.v1.TicketAndRuleType;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.rules.AlarmMovement;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.rules.AlarmMovementList;
import eu.europa.ec.fisheries.uvms.reporting.service.helper.AlarmHelper;
import eu.europa.ec.fisheries.uvms.reporting.service.helper.GeoJsonBuilder;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by padhyad on 3/25/2016.
 */
@Stateless
@LocalBean
@Slf4j
public class AlarmServiceBean {

    @EJB
    private RulesEventService rulesModule;

    @EJB
    private AssetServiceBean assetService;

    private static final String LONGITUDE = "longitude";

    private static final String LATITUDE = "latitude";

    private static final String TYPE = "Point";

    public ObjectNode getAlarmsForMovements(AlarmMovementList alarmMovementList, String userName) throws ReportingServiceException {
        List<String> movementIds = getMovementIds(alarmMovementList.getAlarmMovementList());
        List<TicketAndRuleType> ticketAndRules = getTicketAndRules(movementIds);
        if (ticketAndRules == null || CollectionUtils.isEmpty(ticketAndRules)) {
            return null;
        }
        return createGeoJsonWithAlarmProperties(ticketAndRules, alarmMovementList.getAlarmMovementList(), userName);
    }

    private List<TicketAndRuleType> getTicketAndRules(List<String> movements) throws ReportingServiceException {
        return rulesModule.GetAlarmsForMovements(movements);
    }

    private ObjectNode createGeoJsonWithAlarmProperties(List<TicketAndRuleType> ticketAndRules, List<AlarmMovement> alarmMovements, String userName) throws ReportingServiceException {
        ticketAndRules = AlarmHelper.removeRestrictedTickets(ticketAndRules, userName);
        List<Asset> assets = getAssets(ticketAndRules);
        GeoJsonBuilder geoJsonBuilder = new GeoJsonBuilder();
        for (TicketAndRuleType ticketAndRule : ticketAndRules) {
            Map<String, Double> coordinates = getPointCoordinates(alarmMovements, ticketAndRule.getTicket().getMovementGuid());
            Map<String, Object> properties = AlarmHelper.getAlarmProperties(ticketAndRule, assets);
            properties.putAll(AlarmHelper.getAssetProperties(assets, ticketAndRule.getTicket().getAssetGuid()));
            geoJsonBuilder.addFeatureToCollection(properties, coordinates);
        }
        return geoJsonBuilder.toJson();
    }

    private List<Asset> getAssets(List<TicketAndRuleType> ticketAndRules) throws ReportingServiceException {
        return assetService.getAssets(AlarmHelper.getAssetListQuery(ticketAndRules));
    }

    private Map<String, Double> getPointCoordinates(List<AlarmMovement> alarmMovements, String movementId) {
        for (AlarmMovement alarmMovement : alarmMovements) {
            if (alarmMovement.getMovementId().equalsIgnoreCase(movementId)) {
                return ImmutableMap.<String, Double>builder().put(LONGITUDE, Double.valueOf(alarmMovement.getxCoordinate())).put(LATITUDE, Double.valueOf(alarmMovement.getyCoordinate())).build();
            }
        }
        return Collections.emptyMap();
    }

    private List<String> getMovementIds(List<AlarmMovement> alarmMovements) {
        List<String> movementIds = new ArrayList<>();
        for (AlarmMovement alarmMovement : alarmMovements) {
            movementIds.add(alarmMovement.getMovementId());
        }
        return movementIds;
    }
}
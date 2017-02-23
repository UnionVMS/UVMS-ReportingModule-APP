/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.bean.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.schema.rules.ticketrule.v1.TicketAndRuleType;
import eu.europa.ec.fisheries.uvms.reporting.bean.RulesEventService;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.rules.AlarmMovement;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.rules.AlarmMovementList;
import eu.europa.ec.fisheries.uvms.reporting.service.util.AlarmHelper;
import eu.europa.ec.fisheries.uvms.reporting.service.util.GeoJsonBuilder;
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
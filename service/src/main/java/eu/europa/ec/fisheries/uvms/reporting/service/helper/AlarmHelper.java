/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.helper;

import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.*;
import eu.europa.ec.fisheries.schema.rules.ticketrule.v1.TicketAndRuleType;
import eu.europa.ec.fisheries.wsdl.asset.types.*;

import java.util.*;

/**
 * Created by padhyad on 3/29/2016.
 */
public class AlarmHelper {

    public static final String NAME = "name";
    public static final String EXTMARK = "extMark";
    public static final String IRCS = "ircs";
    public static final String CFR = "cfr";
    public static final String FS = "fs";
    public static final String TICKET_STATUS = "ticketStatus";
    public static final String TICKET_OPENDATE = "ticketOpenDate";
    public static final String TICKET_UPDATEDATE = "ticketUpdateDate";
    public static final String TICKET_UPDATEDBY = "ticketUpdatedBy";
    public static final String RULE_NAME = "ruleName";
    public static final String RULE_DESC = "ruleDesc";
    public static final String RULE_DEFINITION = "ruleDefinitions";


    public static List<TicketAndRuleType> removeRestrictedTickets(List<TicketAndRuleType> ticketAndRules, String userName) {
        for (Iterator<TicketAndRuleType> iterator = ticketAndRules.listIterator(); iterator.hasNext();) {
            TicketAndRuleType ticketAndRule = iterator.next();
            AvailabilityType availabilityType = ticketAndRule.getRule().getAvailability();
            switch (availabilityType) {
                case PUBLIC:
                    break;
                case GLOBAL:
                    break;
                case PRIVATE:
                    if (ticketAndRule.getRule().getSubscriptions() != null) {
                        List<SubscriptionType> subscriptionTypes = ticketAndRule.getRule().getSubscriptions();
                        Boolean isTicketToRemove = true;
                        for (SubscriptionType subscriptionType : subscriptionTypes) {
                            if (subscriptionType.getOwner().equalsIgnoreCase(userName) && subscriptionType.getType().equals(SubscriptionTypeType.TICKET)) {
                                isTicketToRemove = false;
                                break;
                            }
                        }
                        if (isTicketToRemove) {
                            iterator.remove();
                        }
                    } else {
                        iterator.remove();
                    }
                    break;
            }
        }
        return ticketAndRules;
    }

    public static Map<String, Object> getAlarmProperties(TicketAndRuleType ticketAndRule, List<Asset> assets) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(TICKET_STATUS, ticketAndRule.getTicket().getStatus().value());
        properties.put(TICKET_OPENDATE, ticketAndRule.getTicket().getOpenDate());
        properties.put(TICKET_UPDATEDATE, ticketAndRule.getTicket().getUpdated());
        properties.put(TICKET_UPDATEDBY, ticketAndRule.getTicket().getUpdatedBy());
        properties.put(RULE_NAME, ticketAndRule.getRule().getName());
        properties.put(RULE_DESC, ticketAndRule.getRule().getDescription());
        properties.put(RULE_DEFINITION, getRuleDefinitions(ticketAndRule.getRule().getDefinitions()));
        return properties;
    }

    private static String getRuleDefinitions(List<CustomRuleSegmentType> definitions) {
        CustomRuleSegmentTypeComparator comparator = new CustomRuleSegmentTypeComparator();
        Collections.sort(definitions, comparator);
        StringBuilder builder = new StringBuilder();
        for (CustomRuleSegmentType customRuleSegmentType : definitions) {
            builder.append(customRuleSegmentType.getStartOperator()).
                    append(customRuleSegmentType.getCriteria()).append(".").
                    append(customRuleSegmentType.getSubCriteria()).append(" ").
                    append(customRuleSegmentType.getCondition()).append(" ").
                    append(customRuleSegmentType.getValue()).
                    append(customRuleSegmentType.getEndOperator());
            if (customRuleSegmentType.getLogicBoolOperator() != null && !customRuleSegmentType.getLogicBoolOperator().equals(LogicOperatorType.NONE)) {
                builder.append(" ").append(customRuleSegmentType.getLogicBoolOperator()).append(" ");
            }
        }
        return builder.toString();
    }

    public static Map<String, Object> getAssetProperties(List<Asset> assets, String assetId) {
        for (Asset asset : assets) {
            if (asset.getAssetId().getGuid().equalsIgnoreCase(assetId)) {
                return ImmutableMap.<String, Object>builder().
                        put(NAME, asset.getName()).
                        put(EXTMARK, asset.getExternalMarking()).
                        put(IRCS, asset.getIrcs()).
                        put(CFR, asset.getCfr()).
                        put(FS, asset.getCountryCode()).build();
            }
        }
        return Collections.emptyMap();
    }

    public static AssetListQuery getAssetListQuery(List<TicketAndRuleType> ticketAndRules) {
        List<AssetListCriteriaPair> assetListCriteriaList = new ArrayList<>();
        for (TicketAndRuleType ticketAndRuleType : ticketAndRules) {
            AssetListCriteriaPair criteria = new AssetListCriteriaPair();
            criteria.setKey(ConfigSearchField.GUID);
            criteria.setValue(ticketAndRuleType.getTicket().getAssetGuid());
            assetListCriteriaList.add(criteria);
        }
        assetListCriteriaList = removeDuplicateAsset(assetListCriteriaList);
        return createAssetListQuery(assetListCriteriaList);
    }

    private static AssetListQuery createAssetListQuery(List<AssetListCriteriaPair> assetListCriteriaList) {
        AssetListCriteria assetListCriteria = new AssetListCriteria();
        assetListCriteria.getCriterias().addAll(assetListCriteriaList);
        assetListCriteria.setIsDynamic(false);

        AssetListPagination assetListPagination = new AssetListPagination();
        assetListPagination.setPage(1);
        assetListPagination.setListSize(assetListCriteriaList.size());

        AssetListQuery assetListQuery = new AssetListQuery();
        assetListQuery.setAssetSearchCriteria(assetListCriteria);
        assetListQuery.setPagination(assetListPagination);
        return assetListQuery;
    }

    private static List<AssetListCriteriaPair> removeDuplicateAsset(List<AssetListCriteriaPair> assetListCriteriaList) {
        List<AssetListCriteriaPair> tempList = new ArrayList<>();
        for (AssetListCriteriaPair assetCriteria : assetListCriteriaList) {
            Boolean isAssetAdded = false;
            for(AssetListCriteriaPair tempCriteria : tempList) {
                if (assetCriteria.getValue().equalsIgnoreCase(tempCriteria.getValue())) {
                    isAssetAdded = true;
                    break;
                }
            }
            if (!isAssetAdded) {
                tempList.add(assetCriteria);
            }
        }
        return tempList;
    }
}
/*
 *
 * Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */


package eu.europa.ec.fisheries.uvms.reporting.service.bean.impl;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jms.TextMessage;
import java.util.List;

import eu.europa.ec.fisheries.uvms.activity.model.exception.ActivityModelMapperException;
import eu.europa.ec.fisheries.uvms.activity.model.mapper.ActivityModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.activity.model.mapper.ActivityModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FACatchSummaryReportResponse;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingTripResponse;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.GroupCriteria;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.ListValueTypeFilter;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.SingleValueTypeFilter;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.service.interceptor.SimpleTracingInterceptor;
import eu.europa.ec.fisheries.uvms.reporting.message.service.ActivityModuleSenderBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.ReportingModuleReceiverBean;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.ActivityService;
import lombok.extern.slf4j.Slf4j;

@Stateless
@Local(ActivityService.class)
@Slf4j
public class ActivityServiceBean implements ActivityService {

    @EJB
    private ActivityModuleSenderBean activityModule;

    @EJB
    private ReportingModuleReceiverBean reportingModule;

    @Override
    @Interceptors(SimpleTracingInterceptor.class)
    public FishingTripResponse getFishingTrips(List<SingleValueTypeFilter> singleValueTypeFilters, List<ListValueTypeFilter> listValueTypeFilters) throws ReportingServiceException {
        try {
            String request = ActivityModuleRequestMapper.mapToActivityGetFishingTripRequest(listValueTypeFilters, singleValueTypeFilters);
            String correlationId = activityModule.sendModuleMessage(request, reportingModule.getDestination());
            TextMessage response = reportingModule.getMessage(correlationId, TextMessage.class);
            if (response != null) {
                return ActivityModuleResponseMapper.mapToActivityFishingTripFromResponse(response, correlationId);
            }
        } catch (MessageException | ActivityModelMapperException e) {
            throw new ReportingServiceException(e.getMessage(), e);
        }
        return null;
    }

    @Override
    @Interceptors(SimpleTracingInterceptor.class)
    public FACatchSummaryReportResponse getFaCatchSummaryReport(List<SingleValueTypeFilter> singleValueTypeFilters, List<ListValueTypeFilter> listValueTypeFilters, List<GroupCriteria> groupCriteriaList) throws ReportingServiceException {

        FACatchSummaryReportResponse result = null;
        try {
            String request = ActivityModuleRequestMapper.mapToFaCatchSummaryReportRequestRequest(listValueTypeFilters, singleValueTypeFilters, groupCriteriaList);
            String correlationId = activityModule.sendModuleMessage(request, reportingModule.getDestination());
            TextMessage response = reportingModule.getMessage(correlationId, TextMessage.class);
            if (response != null) {
                result = ActivityModuleResponseMapper.mapToFaCatchSummaryResponseFromResponse(response, correlationId);
            }
        } catch (MessageException | ActivityModelMapperException e) {
            throw new ReportingServiceException(e.getMessage(), e);
        }
        return result;
    }
}

/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.bean;

import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingTripResponse;
import eu.europa.ec.fisheries.uvms.domain.DateRange;
import eu.europa.ec.fisheries.uvms.reporting.bean.impl.ActivityServiceBean;
import eu.europa.ec.fisheries.uvms.reporting.bean.impl.AssetServiceBean;
import eu.europa.ec.fisheries.uvms.reporting.bean.impl.MovementServiceBean;
import eu.europa.ec.fisheries.uvms.reporting.bean.impl.ReportExecutionServiceBean;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.entities.AssetFilter;
import eu.europa.ec.fisheries.uvms.reporting.entities.AssetGroupFilter;
import eu.europa.ec.fisheries.uvms.reporting.entities.CommonFilter;
import eu.europa.ec.fisheries.uvms.reporting.entities.FaFilter;
import eu.europa.ec.fisheries.uvms.reporting.entities.FaWeight;
import eu.europa.ec.fisheries.uvms.reporting.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.entities.PositionSelector;
import eu.europa.ec.fisheries.uvms.reporting.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.entities.Selector;
import eu.europa.ec.fisheries.uvms.reporting.enums.ReportTypeEnum;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.jms.TextMessage;
import lombok.SneakyThrows;
import org.joda.time.DateTime;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;
import org.unitils.mock.MockUnitils;
import org.unitils.mock.PartialMock;

public class ReportExecutionServiceBeanTest extends UnitilsJUnit4 {

    @TestedObject
    private ReportExecutionServiceBean service;

    @InjectIntoByType
    private Mock<SpatialService> spatialModule;

    @InjectIntoByType
    private Mock<ReportRepository> repository;

    @InjectIntoByType
    private Mock<AssetServiceBean> asset;

    @InjectIntoByType
    private Mock<MovementServiceBean> movement;

    @InjectIntoByType
    private Mock<ActivityServiceBean> activity;

    private Mock<Report> report;

    private Mock<FishingTripResponse> response;

    private PartialMock<TextMessage> assetResponse;

    @Test
    @SneakyThrows
    public void testGetVmsDataByReportId() {

        Set<Filter> filterSet = new HashSet<>();
        filterSet.add(AssetFilter.builder().guid("1234").build());
        filterSet.add(CommonFilter.builder().dateRange(new DateRange(new Date(), new Date())).positionSelector(new PositionSelector(null, Selector.all, null)).build());

        report.returns(ReportTypeEnum.STANDARD).getReportType();
        report.returns(filterSet).getFilters();

        asset.returns(ImmutableMap.<String, Asset>builder().build()).getAssetMap(null);

        activity.returns(response.getMock()).getFishingTrips(null, null);
        repository.returns(report.getMock()).findReportByReportId(null, "userName", null, false);

        service.getReportExecutionByReportId(null, "userName", "scope", null, null, false, true);

        asset.assertInvokedInSequence().getAssetMap(null);
        movement.assertInvokedInSequence().getMovement(null);
        activity.assertInvokedInSequence().getFishingTrips(null, null);
        report.assertInvokedInSequence().updateExecutionLog("userName");
        MockUnitils.assertNoMoreInvocations();
    }

    @Test
    @SneakyThrows
    public void testGetVmsDataByReportIdWithAssetGroupFilters() {

        Set<Filter> filterSet = new HashSet<>();
        filterSet.add(AssetGroupFilter.builder().groupId("123").build());
        filterSet.add(CommonFilter.builder().dateRange(new DateRange(new Date(), new Date())).positionSelector(new PositionSelector(null, Selector.all, null)).build());

        report.returns(ReportTypeEnum.STANDARD).getReportType();
        report.returns(filterSet).getFilters();
        asset.returns(ImmutableMap.<String, Asset>builder().build()).getAssetMap(null);
        activity.returns(response.getMock()).getFishingTrips(null, null);
        repository.returns(report.getMock()).findReportByReportId(null, "test", null, false);
        service.getReportExecutionByReportId(null, "test", null, null, null, false, true);

        asset.assertInvokedInSequence().getAssetMap(null);
        movement.assertInvokedInSequence().getMovement(null);
        activity.assertInvokedInSequence().getFishingTrips(null, null);
        report.assertInvokedInSequence().updateExecutionLog("test");

        MockUnitils.assertNoMoreInvocations();

    }

    @Test
    @SneakyThrows
    public void testGetVmsDataByReportIdWithoutAsset() {

        Set<Filter> filterSet = new HashSet<>();
        filterSet.add(CommonFilter.builder().dateRange(new DateRange(new Date(), new Date())).positionSelector(new PositionSelector(null, Selector.all, null)).build());

        report.returns(ReportTypeEnum.STANDARD).getReportType();
        report.returns(filterSet).getFilters();
        asset.returns(ImmutableMap.<String, Asset>builder().build()).getAssetMap(null);
        activity.returns(response.getMock()).getFishingTrips(null, null);
        repository.returns(report.getMock()).findReportByReportId(null, "test", null, false);
        service.getReportExecutionByReportId(null, "test", null, null, DateTime.now(), false, true);

        movement.assertInvokedInSequence().getMovementMap(null);
        asset.assertInvokedInSequence().getAssetMap(null);
        activity.assertInvokedInSequence().getFishingTrips(null, null);
        report.assertInvokedInSequence().updateExecutionLog("test");

        MockUnitils.assertNoMoreInvocations();

    }

    @Test
    @SneakyThrows
    public void testGetVmsDataByReportIdWithFaFilters() {
        Set<Filter> filterSet = new HashSet<>();
        filterSet.add(CommonFilter.builder().dateRange(new DateRange(new Date(), new Date())).positionSelector(new PositionSelector(null, Selector.all, null)).build());
        filterSet.add(FaFilter.builder().reportTypes(Arrays.asList("NOTIFICATION")).
                activityTypes(Arrays.asList("DEPARTURE")).
                masters(Arrays.asList("master1")).
                species(Arrays.asList("Species1")).faGears(Arrays.asList("gear1")).
                faPorts(Arrays.asList("port1")).faWeight(new FaWeight(10.0, 20.0, "KG")).build());

        report.returns(ReportTypeEnum.STANDARD).getReportType();
        report.returns(filterSet).getFilters();
        asset.returns(ImmutableMap.<String, Asset>builder().build()).getAssetMap(null);
        activity.returns(response.getMock()).getFishingTrips(null, null);
        repository.returns(report.getMock()).findReportByReportId(null, "test", null, false);
        service.getReportExecutionByReportId(null, "test", null, null, DateTime.now(), false, true);

        movement.assertInvokedInSequence().getMovementMap(null);
        asset.assertInvokedInSequence().getAssetMap(null);
        activity.assertInvokedInSequence().getFishingTrips(null, null);
        report.assertInvokedInSequence().updateExecutionLog("test");

        MockUnitils.assertNoMoreInvocations();
    }

    @Test(expected = ReportingServiceException.class)
    @SneakyThrows
    public void testReportNull() {
        service.getReportExecutionByReportId(null, "test", null, null, null, false, true);
    }
}
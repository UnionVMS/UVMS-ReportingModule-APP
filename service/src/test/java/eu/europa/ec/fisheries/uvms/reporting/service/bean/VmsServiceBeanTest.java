package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.CommonFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.PositionSelector;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Selector;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselGroupFilter;
import lombok.SneakyThrows;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;
import org.unitils.mock.MockUnitils;
import org.unitils.mock.PartialMock;

import javax.jms.TextMessage;
import java.util.HashSet;
import java.util.Set;

public class VmsServiceBeanTest extends UnitilsJUnit4 {

    @TestedObject
    private VmsServiceBean service;

    @InjectIntoByType
    private Mock<ReportRepository> repository;

    @InjectIntoByType
    private Mock<VesselServiceBean> vessel;

    @InjectIntoByType
    private Mock<MovementServiceBean> movement;

    private Mock<Report> report;

    private PartialMock<TextMessage> vesselResponse;

    @Test
    @SneakyThrows
    public void testGetVmsDataByReportId() {

        Set<Filter> filterSet = new HashSet<>();
        filterSet.add(VesselFilter.builder().guid("1234").build());

        report.returns(filterSet).getFilters();

        vessel.returns(ImmutableMap.<String, String>builder().build()).getVesselMap(null);
        repository.returns(report.getMock()).findReportByReportId(null, "userName", null);

        service.getVmsDataByReportId("userName", "scope",  null);

        vessel.assertInvokedInSequence().getVesselMap(null);
        movement.assertInvokedInSequence().getMovement(null);
        report.assertInvokedInSequence().updateExecutionLog("userName");
        MockUnitils.assertNoMoreInvocations();
    }

    @Test
    @SneakyThrows
    public void testGetVmsDataByReportIdWithVesselGroupFilters() {

        Set<Filter> filterSet = new HashSet<>();
        filterSet.add(VesselGroupFilter.builder().groupId("123").build());

        report.returns(filterSet).getFilters();
        vessel.returns(ImmutableMap.<String, String>builder().build()).getVesselMap(null);
        repository.returns(report.getMock()).findReportByReportId(null, "test", null);
        service.getVmsDataByReportId("test", null, null);

        vessel.assertInvokedInSequence().getVesselMap(null);
        movement.assertInvokedInSequence().getMovement(null);
        report.assertInvokedInSequence().updateExecutionLog("test");

        MockUnitils.assertNoMoreInvocations();

    }

    @Test
    @SneakyThrows
    public void testGetVmsDataByReportIdWithoutVessel() {

        Set<Filter> filterSet = new HashSet<>();
        filterSet.add(CommonFilter.builder()
                .positionSelector(PositionSelector.builder().selector(Selector.all).build())
                .build());

        report.returns(filterSet).getFilters();
        vessel.returns(ImmutableMap.<String, String>builder().build()).getVesselMap(null);
        repository.returns(report.getMock()).findReportByReportId(null, "test", null);
        service.getVmsDataByReportId("test", null, null);

        movement.assertInvokedInSequence().getMovementMap(null);
        vessel.assertInvokedInSequence().getVesselMap(null);
        report.assertInvokedInSequence().updateExecutionLog("test");

        MockUnitils.assertNoMoreInvocations();

    }

    @Test(expected = ReportingServiceException.class)
    @SneakyThrows
    public void testReportNull() {
        service.getVmsDataByReportId("test", null, null);
    }
}
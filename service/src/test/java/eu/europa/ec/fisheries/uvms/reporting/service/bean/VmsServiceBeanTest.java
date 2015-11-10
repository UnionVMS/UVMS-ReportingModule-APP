package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.uvms.reporting.message.service.VesselModuleReceiverBean;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.*;
import lombok.SneakyThrows;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;
import org.unitils.mock.MockUnitils;
import org.unitils.mock.PartialMock;

import javax.ejb.EJB;
import javax.jms.TextMessage;
import java.util.HashSet;
import java.util.Set;

import static eu.europa.ec.fisheries.uvms.reporting.service.entities.CommonFilter.CommonFilterBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.entities.PositionSelector.PositionSelectorBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselFilter.VesselFilterBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselGroupFilter.VesselGroupFilterBuilder;

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
        filterSet.add(VesselFilterBuilder().guid("1234").build());

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
        filterSet.add(VesselGroupFilterBuilder().groupId("123").build());

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
        filterSet.add(CommonFilterBuilder()
                .positionSelector(PositionSelectorBuilder().selector(Selector.all).build())
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

    @Test
    @SneakyThrows
    public void testWithVesselsAndVesselGroup(){

        //vesselResponse.returns().getText();

        vessel.returns(ImmutableMap.<String, String>builder().build()).getVesselMap(null);


    }

    @Test(expected = ReportingServiceException.class)
    @SneakyThrows
    public void testReportNull() {
        service.getVmsDataByReportId("test", null, null);
    }
}
package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import lombok.SneakyThrows;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;
import org.unitils.mock.MockUnitils;

import java.util.HashSet;
import java.util.Set;

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

    @Test
    @SneakyThrows
    public void testGetVmsDataByReportId() {

        Set<Filter> filterSet = new HashSet<>();
        filterSet.add(VesselFilterBuilder().guid("1234").build());

        report.returns(filterSet).getFilters();

        vessel.returns(ImmutableMap.<String, String>builder().build()).getVesselMapByGuid(null);
        repository.returns(report.getMock()).findReportByReportId(null, "userName", null);

        service.getVmsDataByReportId("userName", "scope",  null);

        vessel.assertInvoked().getVesselMapByGuid(null);
        movement.assertInvoked().getMovementMapResponseTypes(null);
        MockUnitils.assertNoMoreInvocations();
    }

    @Test
    public void testGetVmsDataByReportIdWithVesselGroupFilters() throws Exception {

        Set<Filter> filterSet = new HashSet<>();
        filterSet.add(VesselGroupFilterBuilder().groupId("123").build());

        report.returns(filterSet).getFilters();
        vessel.returns(ImmutableMap.<String, String>builder().build()).getVesselMapByGuid(null);
        repository.returns(report).findReportByReportId(null, "test", null);
        service.getVmsDataByReportId("test", null, null);

        vessel.assertInvoked().getVesselMapByGuid(null);
        movement.assertInvoked().getMovementMapResponseTypes(null);
        MockUnitils.assertNoMoreInvocations();

    }

    @Test(expected = ReportingServiceException.class)
    @SneakyThrows
    public void testReportNull() {
        service.getVmsDataByReportId("test", null, null);
    }
}
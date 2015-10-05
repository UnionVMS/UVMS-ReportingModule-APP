package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.reporting.message.service.MovementMessageService;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.reporting.message.service.VesselMessageService;
import eu.europa.ec.fisheries.uvms.reporting.message.service.MovementMessageServiceBean;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.message.service.VesselMessageServiceBean;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.ReportDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.vessel.model.exception.VesselModelMapperException;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselGroupFilter;
import org.junit.Ignore;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;

import javax.jms.JMSException;
import java.util.ArrayList;
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
    private Mock<VesselMessageService> vesselModule;

    @InjectIntoByType
    private Mock<MovementMessageService> movementModule;

    private Mock<Report> report;

    @Test
    @Ignore
    public void testGetVmsDataByReportId() throws VesselModelMapperException, MessageException, ModelMapperException, JMSException, ReportingServiceException {

        Set<Filter> filterSet = new HashSet<>();
        filterSet.add(VesselFilterBuilder().guid("1234").build());

        report.returns(filterSet).getFilters();
        //FIXME ... the commented lines
      //  vesselModule.returns(new ArrayList<>()).getVesselsByVesselListQuery(null, "usernam");
        //repository.returns(report.getMock()).findReportByReportId(null);

        //service.getVmsDataByReportId("test", null);

        vesselModule.assertInvoked().getVesselsByVesselListQuery(null);
        vesselModule.assertNotInvoked().getVesselsByVesselGroups(null);
        movementModule.assertInvoked().getMovementMap(null);
        report.assertInvoked().updateExecutionLog("test");
    }

    //FIXME fix the test and the commented stuff
    @Test
    @Ignore
    public void testGetVmsDataByReportIdWithVesselGroupFilters() throws Exception {

        Set<Filter> filterSet = new HashSet<>();
        filterSet.add(VesselGroupFilterBuilder().groupId("123").build());

        report.returns(filterSet).getFilters();
        vesselModule.returns(new ArrayList<>()).getVesselsByVesselGroups(null);
//        repository.returns(report).findReportByReportId(null);
//
//        service.getVmsDataByReportId("test", null);

        vesselModule.assertNotInvoked().getVesselsByVesselListQuery(null);
        vesselModule.assertInvoked().getVesselsByVesselGroups(null);
        movementModule.assertInvoked().getMovementMap(null);
        report.assertInvoked().updateExecutionLog("test");

    }

    @Test(expected = ReportingServiceException.class)
    @Ignore
    //FIXME fix the test and the commented stuff
    public void testReportNull() throws ReportingServiceException {
//        service.getVmsDataByReportId("test", null);
    }
}
package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.reporting.message.service.ReportingModuleReceiverBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.VesselModuleSenderBean;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.FilterProcessor;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListQuery;
import lombok.SneakyThrows;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;
import org.unitils.mock.MockUnitils;
import org.unitils.mock.PartialMock;
import javax.jms.Destination;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.HashSet;

public class VesselServiceBeanTest extends UnitilsJUnit4 {

    @TestedObject
    private PartialMock<VesselServiceBean> service;

    @InjectIntoByType
    private Mock<VesselModuleSenderBean> vesselSender;

    @InjectIntoByType
    private Mock<ReportingModuleReceiverBean> vesselReceiver;

    private Mock<FilterProcessor> processor;

    private Mock<Destination> destination;

    private Mock<TextMessage> message;

    @Test
    @SneakyThrows
    public void getVesselMapWithVessel(){

        processor.returns(true).hasVessels();
        processor.returns(new VesselListQuery()).toVesselListQuery();
        vesselReceiver.returns(message).getMessage(null, null);
        service.returns(new ArrayList<>()).getVessels(null, null);

        service.getMock().getVesselMap(processor.getMock());

        vesselSender.assertInvokedInSequence().sendModuleMessage(null, null);
        vesselReceiver.assertInvokedInSequence().getMessage(null, null);

        MockUnitils.assertNoMoreInvocations();

    }

    @Test
    @SneakyThrows
    public void getVesselMapWithVesselsAndVesselGroup(){

        processor.returns(true).hasVessels();
        processor.returns(true).hasVesselGroups();

        processor.returns(new VesselListQuery()).toVesselListQuery();
        processor.returns(new HashSet<>()).getVesselGroupList();

        vesselReceiver.returns(message).getMessage(null, null);
        service.returns(new ArrayList<>()).getVessels(null, null);

        service.getMock().getVesselMap(processor.getMock());

        vesselSender.assertInvokedInSequence().sendModuleMessage(null, null);
        vesselReceiver.assertInvokedInSequence().getMessage(null, null);

        vesselSender.assertInvokedInSequence().sendModuleMessage(null, null);
        vesselReceiver.assertInvokedInSequence().getMessage(null, null);

        MockUnitils.assertNoMoreInvocations();

    }

}

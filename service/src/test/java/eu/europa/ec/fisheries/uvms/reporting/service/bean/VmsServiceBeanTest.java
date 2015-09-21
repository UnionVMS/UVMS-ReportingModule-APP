package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.reporting.message.service.MovementMessageServiceBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.VesselMessageServiceBean;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingModelMarshallException;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.ReportDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterExpression;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportEntity;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.ReportingJSONMarshaller;
import eu.europa.ec.fisheries.uvms.vessel.model.exception.VesselModelMapperException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jms.JMSException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VmsServiceBeanTest {

    @InjectMocks
    private VmsServiceBean service = new VmsServiceBean();

    @Mock
    private ReportDAO dao;

    @Mock
    private VesselMessageServiceBean vesselModule;

    @Mock
    private MovementMessageServiceBean movementModule;

    @Mock
    private ReportingJSONMarshaller marshaller;

    @Test
    @Ignore
    public void testGetVmsDataByReportId() throws ReportingModelMarshallException, VesselModelMapperException, MessageException, ModelMapperException, JMSException {

        Long reportId = 1L;
        FilterExpression filterMock = mock(FilterExpression.class);
        ReportEntity entityMock = mock(ReportEntity.class);

        when(dao.findById(reportId)).thenReturn(entityMock);
        when(marshaller.marshall(null, FilterExpression.class)).thenReturn(filterMock);
        when(marshaller.marshall(null, FilterExpression.class)).thenReturn(filterMock);

        service.getVmsDataByReportId(reportId);

        verify(dao, times(1)).findById(reportId);
        verify(marshaller, times(1)).marshall(null, FilterExpression.class);
        verify(vesselModule, times(1)).getStringVesselMapByGuid(null);
        verify(movementModule, times(1)).getMovementMap(null);

    }
}

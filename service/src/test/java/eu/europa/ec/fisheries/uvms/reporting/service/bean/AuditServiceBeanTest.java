package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import eu.europa.ec.fisheries.uvms.common.AuditActionEnum;
import eu.europa.ec.fisheries.uvms.reporting.message.producer.bean.AuditProducerBean;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;

@RunWith(MockitoJUnitRunner.class)
public class AuditServiceBeanTest {
	
	@Mock
	AuditProducerBean auditProducerBean;
	
	@InjectMocks
	AuditServiceBean auditServiceBean;
	
	@Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
	
	@Test
	public void testInterceptor() {
		try {
			auditServiceBean.sendAuditReport(AuditActionEnum.CREATE, "123");
			auditServiceBean.sendAuditReport(AuditActionEnum.MODIFY, "123");
			auditServiceBean.sendAuditReport(AuditActionEnum.DELETE, "123");
		} catch (ReportingServiceException e) {
			assertNull(e);
		}
		
	}

}

/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import static org.junit.Assert.assertNull;

import eu.europa.ec.fisheries.uvms.reporting.service.bean.impl.AuditServiceBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.AuditMessageServiceBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import eu.europa.ec.fisheries.uvms.commons.service.interceptor.AuditActionEnum;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;

@RunWith(MockitoJUnitRunner.class)
public class AuditServiceBeanTest {
	
	@Mock
    AuditMessageServiceBean auditProducerBean;
	
	@InjectMocks
    AuditServiceBean auditServiceBean;
	
	@Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
	
	@Test
	public void testInterceptor() {
		try {
			auditServiceBean.sendAuditReport(AuditActionEnum.CREATE, "123", "test");
			auditServiceBean.sendAuditReport(AuditActionEnum.MODIFY, "123", "test");
			auditServiceBean.sendAuditReport(AuditActionEnum.DELETE, "123", "test");
		} catch (ReportingServiceException e) {
			assertNull(e);
		}
		
	}

}
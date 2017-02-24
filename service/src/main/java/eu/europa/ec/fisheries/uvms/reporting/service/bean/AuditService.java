/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.common.AuditActionEnum;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;

import java.io.Serializable;

/**
 * Audit Service interface to provide entry point methods for all the services related to Audit module
 *
 */
public interface AuditService extends Serializable{
	
	/**
	 * Send Audit report to Audit module for CRUD operations on Report
	 * 
	 * @param auditActionEnum {@link AuditActionEnum}
	 * @param objectId {@link String}
	 * @param username User Name
	 * @throws ReportingServiceException {@link ReportingServiceException}
	 */
	public void sendAuditReport(final AuditActionEnum auditActionEnum, final String objectId, final String username) throws ReportingServiceException;

}
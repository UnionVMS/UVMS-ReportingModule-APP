/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.bean;

import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.report.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ExecutionResultDTO;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import org.joda.time.DateTime;

import java.util.List;

public interface ReportExecutionService {

    ExecutionResultDTO getReportExecutionByReportId(Long id, String username, String scopeName, List<AreaIdentifierType> areaRestrictions, DateTime now, Boolean isAdmin, Boolean withActivity) throws ReportingServiceException;

    ExecutionResultDTO getReportExecutionWithoutSave(Report report, List<AreaIdentifierType> areaRestrictions, String userName, Boolean withActivity) throws ReportingServiceException;

}
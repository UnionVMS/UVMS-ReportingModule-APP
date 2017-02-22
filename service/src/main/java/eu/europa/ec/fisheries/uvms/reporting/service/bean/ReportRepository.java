/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.report.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.report.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;

import java.util.List;

public interface ReportRepository {

    boolean update(ReportDTO reportDTO) throws ReportingServiceException;

    Report findReportByReportId(Long reportId, String username, String scopeName, Boolean isAdmin) throws ReportingServiceException;

    Report findReportByReportId(Long reportId) throws ReportingServiceException, ServiceException;

    List<Report> listByUsernameAndScope(String username, String scopeName, Boolean existent, Boolean isAdmin) throws ReportingServiceException;

    void remove(Long reportId, String username, String scopeName, Boolean isAdmin) throws ReportingServiceException;

    Report createEntity(Report reportEntity) throws ReportingServiceException;

    void changeVisibility(Long reportId, VisibilityEnum newVisibility, String username, String scopeName, Boolean isAdmin) throws ReportingServiceException;

    List<Report> listTopExecutedReportByUsernameAndScope(String username, String scopeName, Boolean existent, boolean isAdmin, Integer numberOfReport) throws ReportingServiceException;
}
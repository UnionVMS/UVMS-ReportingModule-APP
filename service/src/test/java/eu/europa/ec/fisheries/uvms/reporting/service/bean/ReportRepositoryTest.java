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
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.ExecutionLogDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.FilterDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.ReportDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.merger.FilterMerger;
import eu.europa.ec.fisheries.uvms.reporting.service.merger.ReportMerger;
import lombok.SneakyThrows;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;

public class ReportRepositoryTest extends UnitilsJUnit4 {

    @TestedObject
    private ReportRepository repository = new ReportRepositoryBean();

    @InjectIntoByType
    private Mock<ReportDAO> reportDAO;

    @InjectIntoByType
    private Mock<FilterDAO> filterDAO;

    @InjectIntoByType
    private Mock<ExecutionLogDAO> executionLogDAO;

    @InjectIntoByType
    private Mock<FilterMerger> filterMerger;

    @InjectIntoByType
    private Mock<ReportMerger> reportMerger;

    private Mock<ReportDTO> reportDTO;

    @Test
    @SneakyThrows
    public void testUpdateMultipleFilters(){

        reportDTO.returns(1).getFilters().size();

        repository.update(reportDTO.getMock());

        reportMerger.assertInvoked().merge(null);
        filterMerger.assertInvoked().merge(null);
    }

    @Test
    @SneakyThrows
    public void testUpdateNoFilters(){

        reportDTO.returns(true).getFilters().isEmpty();

        repository.update(reportDTO.getMock());

        reportMerger.assertInvoked().merge(null);
        filterMerger.assertNotInvoked().merge(null);
    }

    @Test(expected = ReportingServiceException.class)
    @SneakyThrows
    public void testUpdateNoFiltersExceptionInReportMerger(){

        reportDTO.returns(1).getFilters().size();
        reportMerger.raises(ServiceException.class).merge(null);

        repository.update(reportDTO.getMock());

        filterMerger.assertNotInvoked().merge(null);

    }

    @Test(expected = ReportingServiceException.class)
    @SneakyThrows
    public void testUpdateNoFiltersExceptionInFilterMerger(){

        reportDTO.returns(1).getFilters().size();
        filterMerger.raises(ServiceException.class).merge(null);

        repository.update(reportDTO.getMock());

        reportMerger.assertInvoked().merge(null);

    }

    @Test
    @SneakyThrows
    public void testFindReportByReportId(){

        repository.findReportByReportId(null, null, null, false);

        reportDAO.assertInvoked().findReportByReportId(null, null, null, false);
    }

    @Test
    @SneakyThrows
    public void testListByUsernameAndScope(){

        repository.listByUsernameAndScope(null, null, null, false);

        reportDAO.assertInvoked().listByUsernameAndScope(null, null, null, false);
    }

    @Test
    @SneakyThrows
    public void testRemove(){

        repository.remove(null, null, null, false);

        reportDAO.assertInvoked().softDelete(null, null, null, false);
    }

    @Test
    @SneakyThrows
    public void testCreate(){

        repository.createEntity(null);

        reportDAO.assertInvoked().createEntity(null);
    }

}
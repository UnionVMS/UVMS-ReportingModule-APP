/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.merger;

import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.ReportDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ExecutionLog;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportDetails;
import lombok.SneakyThrows;
import org.junit.Ignore;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.HashSet;

import static eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO.ReportDTOBuilder;
import static junit.framework.TestCase.assertTrue;

public class ReportMergerTest extends UnitilsJUnit4 {

    @PersistenceContext
    private EntityManager em;

    @TestedObject
    private ReportMerger merger = new ReportMerger(em);

    @InjectIntoByType
    private Mock<ReportDAO> daoMock;

    @Test
    @SneakyThrows
        public void shouldUpdateWhenValuesModified(){

        Report existingReport = Report.builder().id(1L).details(ReportDetails.builder().description("desc").createdBy("me").build()).build();
        ReportDTO incomingReport = ReportDTOBuilder().id(1L).createdBy("you").description("desc").build();

        daoMock.returns(existingReport).findEntityById(Report.class, null);

        boolean updated = merger.merge(Arrays.asList(incomingReport));

        daoMock.assertInvoked().updateEntity(existingReport);
        daoMock.assertNotInvoked().deleteEntity(Report.class, 1L);
        daoMock.assertNotInvoked().createEntity(existingReport);

        assertTrue(updated);

    }

    @Test
    @SneakyThrows
    public void shouldNotUpdateWhenNothingHasChanged(){

        Report existingReport = Report.builder()
                .id(1L)
                .executionLogs(new HashSet<ExecutionLog>())
                .details(ReportDetails.builder()
                        .description("desc")
                        .createdBy("you")
                        .build())
                .build();

        ReportDTO incomingReport = ReportDTOBuilder().id(1L).
                visibility(VisibilityEnum.PRIVATE).createdBy("you").description("desc").build();

        daoMock.returns(existingReport).findEntityById(Report.class, null);

        boolean updated = merger.merge(Arrays.asList(incomingReport));

        daoMock.assertNotInvoked().updateEntity(existingReport);
        daoMock.assertNotInvoked().deleteEntity(Report.class, 1L);
        daoMock.assertNotInvoked().createEntity(existingReport);

        assertTrue(!updated);

    }
}
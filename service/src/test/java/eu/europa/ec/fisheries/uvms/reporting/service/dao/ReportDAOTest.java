/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.dao;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Audit;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportDetails;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static junit.framework.TestCase.*;

@Slf4j
public class ReportDAOTest extends BaseReportingDAOTest {

    private ReportDAO dao = new ReportDAO(em);

    protected DbSetupTracker dbSetupTracker = new DbSetupTracker();

    @Before
    public void prepare(){

        Operation operation =
                sequenceOf(
                        DELETE_ALL,
                        INSERT_REFERENCE_DATA,
                        insertInto("reporting.report")
                                .columns("ID", ReportDetails.CREATED_BY, ReportDetails.NAME, Audit.CREATED_ON, ReportDetails.WITH_MAP, Report.VISIBILITY, "is_deleted", ReportDetails.SCOPE_NAME)
                                .values(1, "testUser", "France", java.sql.Date.valueOf("2014-12-12"), '1', VisibilityEnum.PRIVATE, 'N', "testScope")
                                .values(2, "testUser", "United States", java.sql.Date.valueOf("2014-12-13"), '1', VisibilityEnum.PRIVATE, 'N', "testScope")
                                .build());


        DbSetup dbSetup = new DbSetup(new DataSourceDestination(ds), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    @SneakyThrows
    public void shouldFindUnDeletedReport(){

        dbSetupTracker.skipNextLaunch();

        assertNotNull(dao.findEntityById(Report.class, 1L));

    }

    @Test
    @SneakyThrows
    public void shouldFindReportByUserNameAndScopeName(){

        dbSetupTracker.skipNextLaunch();

        assertNotNull(dao.findReportByReportId(1L, "testUser", "testScope", false));

    }

    @Test
    @SneakyThrows
    public void shouldNotFindReportWhenSoftDeleted(){

        dbSetupTracker.skipNextLaunch();

        dao.softDelete(1L, "testUser", "testScope", false);

        assertNull(dao.findReportByReportId(1L, "testUser", "testScope", false));

    }

    @Test
    @SneakyThrows
    public void shouldReturnListOfWithTwoReport(){

        dbSetupTracker.skipNextLaunch();

        List<Report> reports = dao.listByUsernameAndScope("testUser", "testScope",  true, false);

        assertEquals(2, reports.size());

    }
}
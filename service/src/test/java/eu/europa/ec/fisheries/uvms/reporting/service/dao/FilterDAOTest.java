/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.dao;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore     //need to set up arq so that we can have a working DB
public class FilterDAOTest extends BaseReportingDAOTest {

    /*protected DbSetupTracker dbSetupTracker = new DbSetupTracker();
    private FilterDAO dao = new FilterDAO(em);*/

    @Before
    public void prepare(){

        /*Operation operation =
                sequenceOf(
                        DELETE_ALL,
                        INSERT_REFERENCE_DATA,
                        insertInto("reporting.report")
                                .columns("ID", ReportDetails.CREATED_BY, ReportDetails.NAME_, Audit.CREATED_ON, ReportDetails.WITH_MAP, VISIBILITY, "is_deleted", ReportDetails.SCOPE_NAME, Report.REPORT_TYPE)
                                .values(1L, "testUser", "France", java.sql.Date.valueOf("2014-12-12"), '1', VisibilityEnum.PRIVATE, 'N', "testScope", ReportTypeEnum.STANDARD)
                                .values(2L, "testUser", "United States", java.sql.Date.valueOf("2014-12-13"), '1', VisibilityEnum.PRIVATE, 'N', "testScope", ReportTypeEnum.STANDARD)
                                .build(),
                        insertInto("reporting.filter")
                                .columns("filter_id", "end_date", "start_date", Filter.REPORT_ID, "filter_type")
                                .values(1L, java.sql.Date.valueOf("2014-12-13"), java.sql.Date.valueOf("2015-12-13"), 1L, "DATETIME")
                                .build());


        DbSetup dbSetup = new DbSetup(new DataSourceDestination(ds), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);*/
    }

    @Test
    @SneakyThrows
    public void shouldReturnOneFilter(){

        //dbSetupTracker.skipNextLaunch();

        //List<Filter> filters = dao.listByReportId(1L);

        //assertEquals(1, filters.size());
    }

    @Test
    @SneakyThrows
    public void shouldDeleteFilter(){

        em.getTransaction().begin();

        //dao.deleteBy(1L);

        em.flush();
        em.getTransaction().commit();

        //List<Filter> filters = dao.listByReportId(1L);

        //assertEquals(0, filters.size());
    }

}
/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.service.dao;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.generator.ValueGenerators;
import com.ninja_squad.dbsetup.operation.Operation;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.comparator.GroupCriteriaFilterSequenceComparator;
import eu.europa.ec.fisheries.uvms.reporting.service.enums.GroupCriteriaType;
import eu.europa.ec.fisheries.uvms.reporting.service.enums.ReportTypeEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.report.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Audit;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.GroupCriteriaFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportDetails;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.*;

@Slf4j
public class ReportDAOTest extends BaseReportingDAOTest {

    private ReportDAO dao = new ReportDAO(em);

    protected DbSetupTracker dbSetupTracker = new DbSetupTracker();

    @Before
    public void prepare(){

        Operation operation = sequenceOf(DELETE_ALL, INSERT_REFERENCE_DATA,
                insertInto("reporting.report")
                        .withGeneratedValue("ID", ValueGenerators.sequence().startingAt(50L).incrementingBy(1))
                        .columns(ReportDetails.CREATED_BY, ReportDetails.NAME, Audit.CREATED_ON, ReportDetails.WITH_MAP, Report.VISIBILITY, "is_deleted", ReportDetails.SCOPE_NAME, Report.REPORT_TYPE)
                        .values("testUser", "France", java.sql.Date.valueOf("2014-12-12"), '1', VisibilityEnum.PRIVATE, 'N', "testScope", ReportTypeEnum.STANDARD)
                        .values("testUser", "United States", java.sql.Date.valueOf("2014-12-13"), '1', VisibilityEnum.PRIVATE, 'N', "testScope", ReportTypeEnum.STANDARD)
                        .build(),
                insertInto("reporting.filter")
                        .row().column("FILTER_ID", 50L)
                        .column("END_DATE", java.sql.Date.valueOf("2014-12-13"))
                        .column("START_DATE", java.sql.Date.valueOf("2015-12-13"))
                        .column("FILTER_TYPE", "DATETIME")
                        .column("REPORT_ID", 50L)
                        .end()
                        .build());


        DbSetup dbSetup = new DbSetup(new DataSourceDestination(ds), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    @SneakyThrows
    public void shouldFindUnDeletedReport(){

        dbSetupTracker.skipNextLaunch();

        assertNotNull(dao.findEntityById(Report.class, 50L));

    }

    @Test
    @SneakyThrows
    public void shouldFindReportByUserNameAndScopeName(){

        dbSetupTracker.skipNextLaunch();

        assertNotNull(dao.findReportByReportId(50L, "testUser", "testScope", false));

    }

    @Test
    @SneakyThrows
    public void shouldNotFindReportWhenSoftDeleted(){

        dbSetupTracker.skipNextLaunch();

        dao.softDelete(50L, "testUser", "testScope", false);

        assertNull(dao.findReportByReportId(50L, "testUser", "testScope", false));

    }

    @Test
    @SneakyThrows
    public void shouldReturnListOfWithTwoReport(){

        dbSetupTracker.skipNextLaunch();

        List<Report> reports = dao.listByUsernameAndScope("testUser", "testScope",  true, false);

        assertEquals(2, reports.size());

    }

    @Test
    @SneakyThrows
    public void shouldSaveGroupByFilter(){

        dbSetupTracker.skipNextLaunch();

        ReportDetails details = ReportDetails.builder().name("name").scopeName("DG_MARE").createdBy("CEDRIC").build();
        Report report = Report.builder().details(details).build();
        GroupCriteriaFilter gear = new GroupCriteriaFilter();
        gear.setCode(GroupCriteriaType.FAO_AREA.name());
        gear.setOrderSequence(1);
        gear.setReport(report);

        GroupCriteriaFilter catchType = new GroupCriteriaFilter();
        catchType.setValues(Arrays.asList(GroupCriteriaType.CATCH_TYPE));
        catchType.setCode("CATCH");
        catchType.setOrderSequence(2);
        catchType.setReport(report);

        report.setFilters(new HashSet<Filter>(Arrays.asList(catchType, gear)));

        em.getTransaction().begin();

        Report savedReport = dao.createEntity(report);

        em.flush();

        Collection criteria = FluentIterable.from(savedReport.getFilters())
                .filter(GroupCriteriaFilter.class)
                .toSortedList(new GroupCriteriaFilterSequenceComparator());

        assertEquals(2, criteria.size());

        GroupCriteriaFilter filter1 = (GroupCriteriaFilter) Iterables.get(criteria, 0);

        assertNotNull(filter1.getId());
        assertNotNull(filter1.getOrderSequence());
        assertNotNull(filter1.getCode());
        assertNull(filter1.getValues());

        GroupCriteriaFilter filter2 = (GroupCriteriaFilter) Iterables.get(criteria, 0);
        assertNotNull(filter2.getId());
        assertNotNull(filter2.getCode());
        assertNotNull(filter2.getOrderSequence());
        assertNull(filter2.getValues());

        System.out.println(savedReport);

    }
}
package eu.europa.ec.fisheries.uvms.reporting.service.dao;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Audit;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportDetails;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static junit.framework.TestCase.assertEquals;

public class FilterDAOTest extends BaseReportingDAOTest {

    private FilterDAO dao = new FilterDAO(em);

    @Before
    public void prepare(){

        Operation operation =
                sequenceOf(
                        DELETE_ALL,
                        INSERT_REFERENCE_DATA,
                        insertInto("reporting.report")
                                .columns("ID", ReportDetails.CREATED_BY, ReportDetails.NAME, Audit.CREATED_ON, ReportDetails.WITH_MAP, Report.VISIBILITY, "is_deleted", ReportDetails.SCOPE_NAME)
                                .values(1L, "testUser", "France", java.sql.Date.valueOf("2014-12-12"), '1', VisibilityEnum.PRIVATE, 'N', "testScope")
                                .values(2L, "testUser", "United States", java.sql.Date.valueOf("2014-12-13"), '1', VisibilityEnum.PRIVATE, 'N', "testScope")
                                .build(),
                        insertInto("reporting.filter")
                                .columns(Filter.FILTER_ID, "end_date", "start_date", Filter.REPORT_ID, "filter_type")
                                .values(1L, java.sql.Date.valueOf("2014-12-13"), java.sql.Date.valueOf("2015-12-13"), 1L, "DATETIME")
                                .build());


        DbSetup dbSetup = new DbSetup(new DataSourceDestination(ds), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    @SneakyThrows
    public void shouldReturnOneFilter(){

        dbSetupTracker.skipNextLaunch();

        List<Filter> filters = dao.listByReportId(1L);

        assertEquals(1, filters.size());
    }

    @Test
    @SneakyThrows
    @Ignore("Doesn't work on Jenkins")
    public void shouldDeleteFilter(){

        dbSetupTracker.skipNextLaunch();

        em.getTransaction().begin();

        dao.deleteBy(1L);

        em.getTransaction().commit();

        List<Filter> filters = dao.listByReportId(1L);

        assertEquals(0, filters.size());
    }

}

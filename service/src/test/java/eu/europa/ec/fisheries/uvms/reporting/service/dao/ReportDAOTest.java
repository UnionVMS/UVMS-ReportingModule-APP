package eu.europa.ec.fisheries.uvms.reporting.service.dao;

import com.ninja_squad.dbsetup.DbSetup;
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

        assertNotNull(dao.findReportByReportId(1L, "testUser", "testScope"));

    }

    @Test
    @SneakyThrows
    public void shouldNotFindReportWhenSoftDeleted(){

        dbSetupTracker.skipNextLaunch();

        dao.softDelete(1L, "testUser", "testScope");

        assertNull(dao.findReportByReportId(1L, "testUser", "testScope"));

    }

    @Test
    @SneakyThrows
    public void shouldReturnListOfWithTwoReport(){

        dbSetupTracker.skipNextLaunch();

        List<Report> reports = dao.listByUsernameAndScope("testUser", "testScope",  true);

        assertEquals(2, reports.size());

    }
}

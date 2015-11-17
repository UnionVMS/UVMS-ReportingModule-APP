package eu.europa.ec.fisheries.uvms.reporting.service.repository;

import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.ReportRepository;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.CommonFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ExecutionLog;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Position;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.PositionSelector;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportDetails;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Selector;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselGroupFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsPositionFilter;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.reflectionassert.ReflectionAssert;

import javax.ejb.EJB;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@Transactional
public class ReportRepositoryIT {

    @EJB
    private ReportRepository repository;

    private Report report;

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "reporting-service.war")
                .addPackages(true, "eu.europa.ec.fisheries.uvms.reporting.service")
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("config.properties")
                .addAsResource("usmDeploymentDescriptor.xml")
                .addAsResource("logback.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

        File[] libs = Maven.resolver().loadPomFromFile("pom.xml").importDependencies(ScopeType.COMPILE, ScopeType.RUNTIME, ScopeType.TEST, ScopeType.PROVIDED).resolve().withTransitivity().asFile();
        war = war.addAsLibraries(libs);

        System.out.println(war.toString(true));

        return war;
    }

    @Before
    public void setUp() throws Exception {

        report = buildReport();
        assertNotNull("ReportDAO not injected", repository);
    }

    private Report buildReport() {
        Date currentDate = new Date();
        return Report.builder()
                .details(ReportDetails.builder().createdBy("georgi")
                        .description("This is a report description created on " + new SimpleDateFormat("yyyy/MM/dd HH:mm").format(currentDate))
                        .name("ReportName" + currentDate.getTime()).withMap(true).scopeName("123").build())
                .filters(new HashSet<Filter>())
                .executionLogs(new HashSet<ExecutionLog>()).build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    @Transactional(value = TransactionMode.ROLLBACK)
    @SuppressWarnings("unchecked")
    public void testRemove() throws Exception {
        Report reportEntity = report;
        reportEntity.setDetails(ReportDetails.builder().name("RemoveTest").build());
        repository.createEntity(reportEntity);

        repository.remove(
                reportEntity.getId(), reportEntity.getDetails().getCreatedBy(), reportEntity.getDetails().getScopeName());

        Report result =
                repository.findReportByReportId(
                        reportEntity.getId(), reportEntity.getDetails().getCreatedBy(), reportEntity.getDetails().getScopeName());

        assertNull(result);

        //now test as entity param

        Report reportEntity2 = buildReport();
        repository.createEntity(reportEntity2);

        repository.remove(
                reportEntity2.getId(), reportEntity2.getDetails().getCreatedBy(), reportEntity2.getDetails().getScopeName());

        result = repository.findReportByReportId(
                reportEntity2.getId(), reportEntity2.getDetails().getCreatedBy(), reportEntity2.getDetails().getScopeName());

        assertNull(result);

    }

    @Test
    @Transactional(value = TransactionMode.ROLLBACK)
    @SuppressWarnings("unchecked")
    public void testFindReportByReportId() throws ReportingServiceException {
        repository.createEntity(report);
        Report reportByReportId = repository.findReportByReportId(
                report.getId(), report.getDetails().getCreatedBy(), report.getDetails().getScopeName());
        assertTrue(reportByReportId.getId() > 0);
        assertNotNull(reportByReportId);
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    @SuppressWarnings("unchecked")
    public void testFindByUsernameAndScope() throws ReportingServiceException {

        report.getDetails().setCreatedBy("georgiTestttt12");
        report.getDetails().setScopeName("945563456");
        repository.createEntity(report);

        Collection<Report> reports = repository.listByUsernameAndScope("georgiTestttt12", "945563456");

        assertNotNull(reports);
        assertTrue(!reports.isEmpty());
        assertEquals(1, reports.size());

        reports = repository.listByUsernameAndScope("georgiTestttt12", "11000");

        assertNotNull(reports);
        assertTrue(reports.isEmpty());

        reports = repository.listByUsernameAndScope("nonexistinguser", "123456");

        assertNotNull(reports);
        assertTrue(reports.isEmpty());

        Report reportEntity2 = buildReport();
        reportEntity2.getDetails().setScopeName("58437239");
        reportEntity2.setVisibility(VisibilityEnum.SCOPE);
        repository.createEntity(reportEntity2);

        Report reportEntity3 = buildReport();
        reportEntity3.setVisibility(VisibilityEnum.PUBLIC);
        repository.createEntity(reportEntity3);

        reports = repository.listByUsernameAndScope("nonexistinguser", "58437239");

        assertNotNull(reports);
        assertTrue(!reports.isEmpty());
        assertEquals(2, reports.size());

        reports = repository.listByUsernameAndScope("nonexistinguser", "123456");

        assertNotNull(reports);
        assertEquals(1, reports.size());
        assertEquals(VisibilityEnum.PUBLIC, reports.iterator().next().getVisibility());

    }

    @SuppressWarnings("unchecked")
    @Test
    @Transactional(value = TransactionMode.ROLLBACK)
    public void testAddReportExecLog() throws ReportingServiceException {

        report.getDetails().setCreatedBy("georgiTestttt12");
        report.getDetails().setScopeName("356456731");

        ExecutionLog repExecLog = new ExecutionLog();
        repExecLog.setExecutedBy("someUser");
        repExecLog.setExecutedOn(new Date());
        repExecLog.setReport(report);

        assertNotNull(report.getExecutionLogs());
        report.getExecutionLogs().add(repExecLog);

        repository.createEntity(report);

        Report result = repository.findReportByReportId(
                report.getId(), report.getDetails().getCreatedBy(), report.getDetails().getScopeName());

        assertNotNull(result);
        assertNotNull(report.getExecutionLogs());
        assertEquals(1, report.getExecutionLogs().size());

    }

    @Test
    @SuppressWarnings("unchecked")
    @Transactional(value = TransactionMode.ROLLBACK)
    public void testLatestReportExecLog() throws ReportingServiceException {

        String user = "georgiTestttt12";
        report.getDetails().setCreatedBy(user);
        report.getDetails().setScopeName("356456731");
        report.setVisibility(VisibilityEnum.PRIVATE);
        ExecutionLog repExecLog = new ExecutionLog();
        repExecLog.setExecutedBy(user);
        repExecLog.setExecutedOn(new Date());
        repExecLog.setReport(report);
        report.getExecutionLogs().add(repExecLog);
        ExecutionLog repExecLog2 = new ExecutionLog();
        repExecLog2.setExecutedBy("otherUser");
        repExecLog2.setExecutedOn(new Date());
        repExecLog2.setReport(report);
        report.getExecutionLogs().add(repExecLog2);
        Report savedReport = repository.createEntity(report);

        List<Report> results = repository.listByUsernameAndScope(user, "356456731");

        Iterator<Report> iterator1 = results.iterator();
        Report foundReport = null;
        while (iterator1.hasNext()) {
            Report next = iterator1.next();
            if (next.getId().equals(savedReport.getId())) {
                foundReport = next;
                break;
            }
        }
        assert foundReport != null;
        assertEquals(user, foundReport.getDetails().getCreatedBy());
        assertEquals("356456731", foundReport.getDetails().getScopeName());
        assertEquals(VisibilityEnum.PRIVATE, foundReport.getVisibility());
        Set<ExecutionLog> executionLogs = foundReport.getExecutionLogs();
        assertEquals(2, executionLogs.size());

    }

    @Test(expected = Exception.class)
    @Transactional(value = TransactionMode.ROLLBACK)
    @SuppressWarnings("unchecked")
    public void testUniqueLogByUser() throws ReportingServiceException {

        String user = "georgiTestttt12";
        report.getDetails().setCreatedBy(user);
        report.getDetails().setScopeName("356456731");
        report.setVisibility(VisibilityEnum.PRIVATE);
        ExecutionLog repExecLog = new ExecutionLog();
        repExecLog.setExecutedBy(user);
        repExecLog.setExecutedOn(new Date());
        repExecLog.setReport(report);
        report.getExecutionLogs().add(repExecLog);
        ExecutionLog repExecLog2 = new ExecutionLog();
        repExecLog2.setExecutedBy(user);
        repExecLog2.setExecutedOn(new Date());
        repExecLog2.setReport(report);
        report.getExecutionLogs().add(repExecLog2);
        repository.createEntity(report);
    }

    @Test
    @Transactional(value = TransactionMode.ROLLBACK)
    public void testCreateFilters() throws ReportingServiceException {

        Set<Filter> expectedCollection = new HashSet<>();

        String user = "georgiTestttt12";
        report.getDetails().setCreatedBy(user);
        report.getDetails().setScopeName("356456731");
        report.setVisibility(VisibilityEnum.PRIVATE);

        VesselFilter filter1 = VesselFilter.builder().build();
        filter1.setReport(report);
        filter1.setGuid("guid1");
        filter1.setName("vessel1");
        expectedCollection.add(filter1);

        VesselFilter filter2 = VesselFilter.builder().build();
        filter2.setReport(report);
        filter2.setGuid("guid2");
        filter2.setName("vessel2");
        expectedCollection.add(filter2);

        VmsPositionFilter filter3 = VmsPositionFilter.builder().build();
        filter3.setMaximumSpeed(123F);
        filter3.setMinimumSpeed(54654F);
        filter3.setReport(report);
        expectedCollection.add(filter3);

        VesselGroupFilter filter4 = new VesselGroupFilter();
        filter4.setReport(report);
        filter4.setGuid("1");
        filter4.setUserName("ffsdfsdfds");
        expectedCollection.add(filter4);

        VesselGroupFilter filter5 = new VesselGroupFilter();
        filter5.setReport(report);
        filter5.setGuid("2");
        filter5.setUserName("ffsdfsdfds");
        expectedCollection.add(filter5);

        CommonFilter filter6 = CommonFilter.builder().build();
        filter6.setReport(report);
        filter6.setPositionSelector(
                PositionSelector.builder().selector(Selector.last).position(Position.hours).value(24F).build()
        );

        ExecutionLog executionLog = new ExecutionLog();
        executionLog.setExecutedOn(new Date());
        executionLog.setExecutedBy("john");
        executionLog.setReport(report);
        report.getExecutionLogs().add(executionLog);

        expectedCollection.add(filter6);

        report.getFilters().add(filter1);
        report.getFilters().add(filter2);
        report.getFilters().add(filter3);
        report.getFilters().add(filter4);
        report.getFilters().add(filter5);
        report.getFilters().add(filter6);

        Report savedReport = repository.createEntity(report);
        Report byId = repository.findReportByReportId(savedReport.getId(), savedReport.getDetails().getCreatedBy(), savedReport.getDetails().getScopeName());

        Set<Filter> filters = byId.getFilters();

        ReflectionAssert.assertReflectionEquals(expectedCollection, filters);
    }

}

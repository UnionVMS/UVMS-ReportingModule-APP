package eu.europa.ec.fisheries.uvms.reporting.service.reporsitory;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.ReportRepository;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.*;
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
import java.util.*;

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
        WebArchive war = ShrinkWrap.create(WebArchive.class,"reporting-service.war")
                .addPackages(true, "eu.europa.ec.fisheries.uvms.reporting.service")
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("config.properties")
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
        return Report.builder().createdBy("georgi").description("This is a report description created on "
                + new SimpleDateFormat("yyyy/MM/dd HH:mm").format(currentDate)).filters(new HashSet<Filter>())
                .name("ReportName" + currentDate.getTime()).outComponents("OutComponents")
                .executionLogs(new HashSet<ExecutionLog>()).scopeId(123).build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    @Transactional(value = TransactionMode.ROLLBACK)
    @SuppressWarnings("unchecked")
    public void testRemove() throws Exception{
        Report reportEntity = report;
        reportEntity.setName("RemoveTest");
        repository.createEntity(reportEntity);

        repository.remove(reportEntity.getId());

        Report result = repository.findReportByReportId(reportEntity.getId());

        assertNull(result);

        //now test as entity param

        Report reportEntity2 = buildReport();
        repository.createEntity(reportEntity2);

        repository.remove(reportEntity2.getId());

        result = repository.findReportByReportId(reportEntity2.getId());

        assertNull(result);

    }

    @Test
    @Transactional(value = TransactionMode.ROLLBACK)
    @SuppressWarnings("unchecked")
    public void testFindReportByReportId() throws ServiceException {
        repository.createEntity(report);
        Report reportByReportId = repository.findReportByReportId(report.getId());
        assertTrue(reportByReportId.getId() > 0);
        assertNotNull(reportByReportId);
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    @SuppressWarnings("unchecked")
    public void testFindByUsernameAndScope() throws ServiceException {

        report.setCreatedBy("georgiTestttt12");
        report.setScopeId(945563456);
        repository.createEntity(report);

        Collection<Report> reports = repository.listByUsernameAndScope("georgiTestttt12", 945563456);

        assertNotNull(reports);
        assertTrue(!reports.isEmpty());
        assertEquals(1, reports.size());

        reports = repository.listByUsernameAndScope("georgiTestttt12", 11000);

        assertNotNull(reports);
        assertTrue(reports.isEmpty());

        reports = repository.listByUsernameAndScope("nonexistinguser", 123456);

        assertNotNull(reports);
        assertTrue(reports.isEmpty());

        Report reportEntity2 = buildReport();
        reportEntity2.setScopeId(58437239);
        reportEntity2.setVisibility(VisibilityEnum.SCOPE);
        repository.createEntity(reportEntity2);

        Report reportEntity3 = buildReport();
        reportEntity3.setVisibility(VisibilityEnum.PUBLIC);
        repository.createEntity(reportEntity3);

        reports = repository.listByUsernameAndScope("nonexistinguser", 58437239);

        assertNotNull(reports);
        assertTrue(!reports.isEmpty());
        assertEquals(2, reports.size());

        reports = repository.listByUsernameAndScope("nonexistinguser", 123456);

        assertNotNull(reports);
        assertEquals(1, reports.size());
        assertEquals(VisibilityEnum.PUBLIC, reports.iterator().next().getVisibility());

    }

    @SuppressWarnings("unchecked")
    @Test
    @Transactional(value = TransactionMode.ROLLBACK)
    public void testAddReportExecLog () throws ServiceException {

        report.setCreatedBy("georgiTestttt12");
        report.setScopeId(356456731);

        ExecutionLog repExecLog = new ExecutionLog();
        repExecLog.setExecutedBy("someUser");
        repExecLog.setExecutedOn(new Date());
        repExecLog.setReport(report);

        assertNotNull(report.getExecutionLogs());
        report.getExecutionLogs().add(repExecLog);

        repository.createEntity(report);

        Report result = repository.findReportByReportId(report.getId());

        assertNotNull(result);
        assertNotNull(report.getExecutionLogs());
        assertEquals(1, report.getExecutionLogs().size());

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testLatestReportExecLog () throws ServiceException {

        String user = "georgiTestttt12";
        report.setCreatedBy(user);
        report.setScopeId(356456731);
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
        Report savedReport = (Report) repository.createEntity(report);

        List<Report> results = repository.listByUsernameAndScope(user, 356456731);

        Iterator<Report> iterator1 = results.iterator();
        Report foundReport = null;
        while (iterator1.hasNext()){
            Report next = iterator1.next();
            if (next.getId() == iterator1.next().getId()){
                foundReport = next;
                break;
            }
        }
        assert foundReport != null;
        assertEquals(user, foundReport.getCreatedBy());
        assertEquals(356456731, foundReport.getScopeId());
        assertEquals(VisibilityEnum.PRIVATE, foundReport.getVisibility());
        Set<ExecutionLog> executionLogs = foundReport.getExecutionLogs();
        assertEquals(2, executionLogs.size());

        repository.deleteEntity(Report.class, savedReport.getId());
    }

    @Test(expected = Exception.class)
    @Transactional(value = TransactionMode.ROLLBACK)
    @SuppressWarnings("unchecked")
    public void testUniqueLogByUser () throws ServiceException {

        String user = "georgiTestttt12";
        report.setCreatedBy(user);
        report.setScopeId(356456731);
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
    @SuppressWarnings("unchecked")
    public void testCreateFilters () throws ServiceException {

        Set<Filter> expectedCollection = new HashSet();

        String user = "georgiTestttt12";
        report.setCreatedBy(user);
        report.setScopeId(356456731);
        report.setVisibility(VisibilityEnum.PRIVATE);

        VesselFilter filter1 = new VesselFilter();
        filter1.setReport(report);
        filter1.setGuid("guid1");
        filter1.setName("vessel1");
        expectedCollection.add(filter1);

        VesselFilter filter2 = new VesselFilter();
        filter2.setReport(report);
        filter2.setGuid("guid2");
        filter2.setName("vessel2");
        expectedCollection.add(filter2);

        VmsPositionFilter filter3 = new VmsPositionFilter();
        filter3.setMaximumSpeed("123");
        filter3.setMinimumSpeed("54654");
        filter3.setReport(report);
        expectedCollection.add(filter3);

        VesselGroupFilter filter4 = new VesselGroupFilter();
        filter4.setReport(report);
        filter4.setGroupId("1");
        filter4.setGuid("516161");
        filter4.setUserName("ffsdfsdfds");
        expectedCollection.add(filter4);

        VesselGroupFilter filter5 = new VesselGroupFilter();
        filter5.setReport(report);
        filter5.setGroupId("2");
        filter5.setGuid("4546");
        filter5.setUserName("ffsdfsdfds");
        expectedCollection.add(filter5);

        report.getFilters().add(filter1);
        report.getFilters().add(filter2);
        report.getFilters().add(filter3);
        report.getFilters().add(filter4);
        report.getFilters().add(filter5);

        Report savedReport = (Report) repository.createEntity(report);
        Report byId = repository.findReportByReportId(savedReport.getId());

        Set<Filter> filters = byId.getFilters();

        ReflectionAssert.assertReflectionEquals(expectedCollection, filters);
    }

}

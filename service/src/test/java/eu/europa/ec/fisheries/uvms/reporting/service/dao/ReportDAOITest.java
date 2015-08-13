package eu.europa.ec.fisheries.uvms.reporting.service.dao;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collection;
import java.util.Date;

import javax.ejb.EJB;

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

import eu.europa.ec.fisheries.uvms.reporting.service.EntityUtil;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportEntity;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportExecutionLogEntity;

@RunWith(Arquillian.class)
@Transactional
public class ReportDAOITest {

  @EJB
  private ReportDAO reportDAO;
  

  @Deployment
  public static WebArchive createDeployment() {
  	WebArchive war = ShrinkWrap.create(WebArchive.class,"reporting-service.war").addPackages(true, "eu.europa.ec.fisheries.uvms.reporting.service")
              .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
              .addAsResource("config.properties")
              .addAsResource("log4j.xml")
              .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

      File[] libs = Maven.resolver().loadPomFromFile("pom.xml").importDependencies(ScopeType.COMPILE, ScopeType.RUNTIME, ScopeType.TEST).resolve().withTransitivity().asFile();
      war = war.addAsLibraries(libs);

      System.out.println(war.toString(true)); 
      
      return war;
  }


	
	@Before
	public void setUp() throws Exception {
		assertNotNull("ReportDAO not injected", reportDAO);
	}

	@After
	public void tearDown() throws Exception {
	}

	
	@Test
	@Transactional(value = TransactionMode.ROLLBACK)
	public void testRemove() {
		ReportEntity reportEntity = EntityUtil.createRandomReportEntity();
		reportEntity.setName("RemoveTest");
		reportDAO.persist(reportEntity);
		
		reportDAO.remove(reportEntity.getId());
		
		ReportEntity result = reportDAO.findById(reportEntity.getId());
		
		assertNull(result);
		
		//now test as entity param
		
		ReportEntity reportEntity2 = EntityUtil.createRandomReportEntity();
		reportDAO.persist(reportEntity2);
		
		reportDAO.remove(reportEntity2);
		
		result = reportDAO.findById(reportEntity2.getId());
		
		assertNull(result);
		
	}

	
	@Test
	@Transactional(value = TransactionMode.ROLLBACK)
	public void testFindById() {
		ReportEntity reportEntity = EntityUtil.createRandomReportEntity();
		reportDAO.persist(reportEntity);
		
		
		assertTrue(reportEntity.getId() > 0);
		
		assertNotNull(reportEntity);
		
	}

	@Test
	@Transactional(TransactionMode.ROLLBACK)
	public void testFindByUsernameAndScope() {
		
		ReportEntity reportEntity = EntityUtil.createRandomReportEntity();
		reportEntity.setCreatedBy("georgiTestttt12");
		reportEntity.setScopeId(356456731);
		reportDAO.persist(reportEntity);
		
		
		Collection<ReportEntity> reports = reportDAO.findByUsernameAndScope("georgiTestttt12", 356456731);
		
		assertNotNull(reports);
		assertTrue(!reports.isEmpty());
		assertEquals(1, reports.size());
		
		
		reports = reportDAO.findByUsernameAndScope("georgiTestttt12", 11000);
		
		assertNotNull(reports);
		assertTrue(reports.isEmpty());
		
		ReportEntity reportEntity2 = EntityUtil.createRandomReportEntity();
		reportEntity2.setScopeId(58437239);
		reportEntity2.setIsShared(true);
		reportDAO.persist(reportEntity2);
		
		
		reports = reportDAO.findByUsernameAndScope("nonexistinguser", 58437239);
		
		assertNotNull(reports);
		assertTrue(!reports.isEmpty());
		assertEquals(1, reports.size());
		
		reports = reportDAO.findByUsernameAndScope("nonexistinguser", 123456);
		
		assertNotNull(reports);
		assertTrue(reports.isEmpty());
		
	}
	
	@SuppressWarnings("deprecation")
	@Test
	@Transactional(value = TransactionMode.ROLLBACK)
	public void testAddReportExecLog () {
		ReportEntity reportEntity = EntityUtil.createRandomReportEntity();
		reportEntity.setCreatedBy("georgiTestttt12");
		reportEntity.setScopeId(356456731);
		
		ReportExecutionLogEntity repExecLog = new ReportExecutionLogEntity();
		repExecLog.setExecutedBy("someUser");
		repExecLog.setExecutedOn(new Date());
		repExecLog.setReport(reportEntity);
		
		assertNotNull(reportEntity.getReportExecutionLogs());
		reportEntity.getReportExecutionLogs().add(repExecLog);
		
		reportDAO.persist(reportEntity);
		
		ReportEntity result = reportDAO.findById(reportEntity.getId());
		
		assertNotNull(result);
		assertNotNull(reportEntity.getReportExecutionLogs());
		assertEquals(1, reportEntity.getReportExecutionLogs().size());
		
	}
	

}

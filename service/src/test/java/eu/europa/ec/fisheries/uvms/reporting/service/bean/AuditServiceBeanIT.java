package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import eu.europa.ec.fisheries.uvms.common.AuditActionEnum;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.ROLLBACK)
public class AuditServiceBeanIT {
	
	@EJB
	AuditService auditService;
	
	@Deployment
	  public static WebArchive createDeployment() {
	  	WebArchive war = ShrinkWrap.create(WebArchive.class,"reporting-service.war").addPackages(true, "eu.europa.ec.fisheries.uvms.reporting.service")
	              .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
	              .addAsManifestResource(new File( "src/test/resources/META-INF/jboss-deployment-structure.xml"))
	              .addAsResource("config.properties")
				  .addAsResource("usmDeploymentDescriptor.xml")
	              .addAsResource("logback.xml");
	  	
	      File[] libs = Maven.resolver().loadPomFromFile("pom.xml").importDependencies(ScopeType.COMPILE, ScopeType.RUNTIME, ScopeType.TEST, ScopeType.PROVIDED).resolve().withTransitivity().asFile();
	      war = war.addAsLibraries(libs);

	      System.out.println(war.toString(true)); 
	      
	      return war;
	  }
		
		@Before
		public void setUp() throws Exception {
			assertNotNull("Audit not injected", auditService);
		}

		@After 
		public void tearDown() throws Exception {
		}
		
		@Test
		@Transactional(TransactionMode.ROLLBACK)
		public void testSendAuditReport() {	
			try {
				auditService.sendAuditReport(AuditActionEnum.CREATE, "123");
				auditService.sendAuditReport(AuditActionEnum.MODIFY, "123");
				auditService.sendAuditReport(AuditActionEnum.DELETE, "123");
			} catch (ReportingServiceException e) {
				assertNull(e);
			}
		}		

}

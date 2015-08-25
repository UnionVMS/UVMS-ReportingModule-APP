package eu.europa.ec.fisheries.uvms.reporting.rest.util;

import java.io.File;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;

public class ArquillianTest {

	 @Deployment(testable = false)
    public static WebArchive createDeployment() {
    	WebArchive war = ShrinkWrap.create(WebArchive.class,"reporting.war").addPackages(true, "eu.europa.ec.fisheries.uvms.reporting.rest")
                //.addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource( new File( "src/main/webapp/WEB-INF/web.xml" ) )
                .addAsManifestResource(new File( "src/main/resources/META-INF/jboss-deployment-structure.xml"))
                .addAsResource("config.properties")
                .addAsResource("logback.xml")
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/beans.xml"));

        File[] libs = Maven.resolver().loadPomFromFile("pom.xml").importDependencies(ScopeType.COMPILE, ScopeType.RUNTIME, ScopeType.TEST).resolve().withTransitivity().asFile();
        war = war.addAsLibraries(libs);

        System.out.println(war.toString(true)); 
        
        return war;
    }
}

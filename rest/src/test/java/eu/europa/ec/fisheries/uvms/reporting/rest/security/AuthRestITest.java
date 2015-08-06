package eu.europa.ec.fisheries.uvms.reporting.rest.security;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import eu.europa.ec.fisheries.uvms.reporting.rest.constants.RestConstants;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.File;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(Arquillian.class)
public class AuthRestITest {

//    @EJB
//    CrudService crudService;
	

    @Deployment
    public static WebArchive createDeployment() {
    	WebArchive war = ShrinkWrap.create(WebArchive.class,"reporting.war").addPackages(true, "eu.europa.ec.fisheries.uvms.reporting.rest")
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource( new File( "src/main/webapp/WEB-INF/web.xml" ) )
                .addAsResource("config.properties")
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/beans.xml"));

        File[] libs = Maven.resolver().loadPomFromFile("pom.xml").importDependencies(ScopeType.COMPILE, ScopeType.RUNTIME, ScopeType.TEST).resolve().withTransitivity().asFile();
        war = war.addAsLibraries(libs);

        System.out.println(war.toString(true)); 
        
        return war;
    }

    @Before
    public void beforeTest() {
//        assertNotNull("CrudService not injected", crudService);
    }

    @Test
    public void noAuthenticationTest() {
    	 Client client = ClientBuilder.newClient();

         WebTarget authTarget = client.target("http://localhost:8080").path("/reporting/rest/report/list");
         Response authResponse = authTarget.queryParam("CURRENT_USER_SCOPE", "DG_MARE").request(MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE).get();

         assertEquals(HttpServletResponse.SC_UNAUTHORIZED, authResponse.getStatus());
         assertEquals("Unauthorized", authResponse.getStatusInfo().getReasonPhrase());

    }
    

}
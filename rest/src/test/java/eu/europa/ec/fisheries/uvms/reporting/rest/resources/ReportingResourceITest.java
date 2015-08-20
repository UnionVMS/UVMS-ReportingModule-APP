package eu.europa.ec.fisheries.uvms.reporting.rest.resources;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.extension.rest.client.Header;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.europa.ec.fisheries.uvms.reporting.rest.constants.RestConstants;
import eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDetailsDTO;
import eu.europa.ec.fisheries.uvms.reporting.rest.util.ArquillianTest;
import eu.europa.ec.fisheries.uvms.reporting.rest.util.EntityUtil;

@RunWith(Arquillian.class)
@RunAsClient
public class ReportingResourceITest extends ArquillianTest {
	
    @ArquillianResource
    URL contextPath;
    

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	@Header(name="connection", value = "Keep-Alive")
	public void testCreateListReadDelete(@ArquillianResteasyResource("rest/report") ResteasyWebTarget webTarget) throws JsonParseException, JsonMappingException, IOException {
		
		//###################  START CREATE TEST
		ReportDetailsDTO reportDetailsDTO = EntityUtil.createRandomReportDetailsDTO();
		reportDetailsDTO.setIsShared(true);
		
		Response response = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.json(reportDetailsDTO));
		
		assertNotNull(response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		assertEquals("success", response.readEntity(String.class));
		
		response.close();
		
		//############ TEST LIST
		Response responseList = webTarget.path("/list").queryParam(RestConstants.REQUEST_PARAM_CURRENT_USER_SCOPE, reportDetailsDTO.getScopeId()).request().get();
		
		assertNotNull(responseList);
		
		assertEquals(HttpServletResponse.SC_OK, responseList.getStatus());
		String foundReports = responseList.readEntity(String.class);
		assertNotNull(foundReports);
		
		ObjectMapper mapper = new ObjectMapper();  
		Collection<ReportDTO> reports = mapper.readValue(foundReports, new TypeReference<Collection<ReportDTO>>() {});
		
		assertEquals(1, reports.size());
		
		ReportDTO report = reports.iterator().next();
		
		assertEquals(reportDetailsDTO.getDesc(), report.getDesc());
		assertEquals(reportDetailsDTO.getName(), report.getName());
		assertNotNull(report.getCreatedOn());
		assertTrue(report.getId()>0);
		assertTrue(report.isDeletable());
		assertTrue(report.isEditable());
		assertTrue(report.getIsShared());
		assertTrue(report.isShareable());
		
		responseList.close();
		
		//###################### TEST READ
		response = webTarget.path("/" + report.getId()).request().get();
		
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		ReportDetailsDTO foundReport = response.readEntity(new GenericType(ReportDetailsDTO.class));
		assertNotNull(foundReports);
		
		assertEquals(report.getDesc(), foundReport.getDesc());
		assertEquals(report.getName(), foundReport.getName());
		assertEquals(report.getId(), foundReport.getId());
		assertEquals(report.getIsShared(), foundReport.getIsShared());
		
		response.close();
		
		//###################### TEST DELETE
		response = webTarget.path("/" + report.getId()).request().delete();
		
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		
		String resultMsg = response.readEntity(String.class);
		assertEquals("success", resultMsg);
		
		response.close();
		
		//###################### TEST READ
		response = webTarget.path("/" + report.getId()).request().get();
		
		assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
		
		response.close();
		
	}
	

}

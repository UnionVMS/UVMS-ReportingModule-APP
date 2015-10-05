package eu.europa.ec.fisheries.uvms.reporting.rest.resources;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import eu.europa.ec.fisheries.uvms.constants.AuthConstants;
import eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDetailsResponseTESTDto;
import eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportResponseTESTDto;
import eu.europa.ec.fisheries.uvms.reporting.rest.util.RestDTOUtil;
import eu.europa.ec.fisheries.uvms.rest.security.JwtTokenHandler;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.extension.rest.client.Header;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.rest.constants.RestConstants;
import eu.europa.ec.fisheries.uvms.reporting.rest.util.ArquillianTest;
import eu.europa.ec.fisheries.uvms.rest.constants.ErrorCodes;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;

@RunWith(Arquillian.class)
@RunAsClient
public class ReportingResourceITest extends ArquillianTest {
	
    @ArquillianResource
    URL contextPath;
	private String authToken;

	@Before
	public void setUp() {
		JwtTokenHandler tokenHandler = new JwtTokenHandler();
		authToken = tokenHandler.createToken("rep_power");
	}

	@After
	public void tearDown() {

	}


	@Test
	@Header(name="connection", value = "Keep-Alive")
	public void testUpdate(@ArquillianResteasyResource("rest/report") ResteasyWebTarget webTarget) throws JsonParseException, JsonMappingException, IOException {
		
		//check if we have the prerequisite - a report in the DB with ID = 1
		Response response = webTarget.path("/1" ).request().header(HttpHeaders.AUTHORIZATION, authToken)
				.header(AuthConstants.HTTP_HEADER_AUTHORIZATION, authToken).header(AuthConstants.HTTP_HEADER_SCOPE_NAME, "123").get();
		
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		ReportResponseTESTDto responseDto = response.readEntity(ReportResponseTESTDto.class);
		assertNotNull(responseDto);

		assertNotNull(responseDto.getData());
		
		response.close();
		
		
		// now let's try to update the entity
		long date = new Date().getTime();
		String payload = "{\"id\":1,"
				+ "\"name\":\"Report Name"+date+"\","
				+ "\"desc\":\"Some description"+date+"\","
				+ "\"visibility\":\"SCOPE\","
				+ "\"scopeName\":\"123\","
				+ "\"outComponents\":\"{\\\"map\\\":"+date+",\\\"vms\\\":true}\","
				+ "\"filterExpression\":\"{\\\"startDate\\\":\\\"2015-09-02 18:20:00\\\",\\\"endDate\\\":\\\"2015-09-02 18:20:00\\\",\\\"positionSelector\\\":\\\""+date+"\\\",\\\"vessels\\\":[{\\\"id\\\":1,\\\"name\\\":\\\"Vessel 1\\\",\\\"type\\\":\\\"vessel\\\"},{\\\"id\\\":2,\\\"name\\\":\\\"Vessel 2\\\",\\\"type\\\":\\\"vessel\\\"}],\\\"vms\\\":{\\\"positions\\\":{\\\"active\\\":false},\\\"segments\\\":{\\\"active\\\":false},\\\"tracks\\\":{\\\"active\\\":false}}}\"}";
		
		ObjectMapper mapper = new ObjectMapper();
		ReportDTO dto = mapper.readValue(payload, ReportDTO.class);
		
		assertNotNull(dto);
		assertNotNull(dto.getOutComponents());
		assertNotNull(dto.getVisibility());
		
		response = webTarget.path("/1" ).request(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authToken)
				.header(AuthConstants.HTTP_HEADER_AUTHORIZATION, authToken).header(AuthConstants.HTTP_HEADER_SCOPE_NAME, dto.getScopeName()).put(Entity.entity(dto,MediaType.APPLICATION_JSON));
		
		assertNotNull(response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		assertEquals("{\"code\":200}", response.readEntity(String.class));
		
		response.close();
		
		//and now let's verify if the update was properly persisted
		response = webTarget.path("/1" ).request().get();
		
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		ReportDetailsResponseTESTDto detailsResponseDTO = response.readEntity(ReportDetailsResponseTESTDto.class);
		assertNotNull(detailsResponseDTO);
		ReportDTO detailsDto = detailsResponseDTO.getData();
		assertEquals("Report Name"+date, detailsDto.getName());
		assertEquals("Some description"+date, detailsDto.getDescription());
		assertEquals(VisibilityEnum.SCOPE, detailsDto.getVisibility());
		assertEquals("{\"map\":"+date+",\"vms\":true}", detailsDto.getOutComponents());
		
		response.close();
		
		
		
	}
	

	@Test
	@Header(name="connection", value = "Keep-Alive")
	public void testCreateListReadDelete(@ArquillianResteasyResource("rest/report") ResteasyWebTarget webTarget) throws IOException {
		
		//###################  START CREATE TEST
		ReportDTO reportDetailsDTO = RestDTOUtil.createRandomReport();
		reportDetailsDTO.setVisibility(VisibilityEnum.SCOPE);
		
		Response response = webTarget.request(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authToken)
				.header(AuthConstants.HTTP_HEADER_AUTHORIZATION, authToken).header(AuthConstants.HTTP_HEADER_SCOPE_NAME, reportDetailsDTO.getScopeName()).post(Entity.json(reportDetailsDTO));
		
		assertNotNull(response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		assertEquals("{\"code\":200}", response.readEntity(String.class));
		
		response.close();
		
		//############ TEST LIST
		
		Collection<ReportDTO> reports = callListRESTAPI(webTarget, reportDetailsDTO.getScopeName());
		
		
		assertEquals(1, reports.size());
		
		ReportDTO report = reports.iterator().next();
		
		assertEquals(reportDetailsDTO.getDescription(), report.getDescription());
		assertEquals(reportDetailsDTO.getName(), report.getName());
		assertNotNull(report.getAudit());
		assertTrue(report.getId()>0);
		assertTrue(report.isDeletable());
		assertTrue(report.isEditable());
		assertEquals(VisibilityEnum.SCOPE, report.getVisibility());
		assertTrue(report.isShareable());
		
		//###################### TEST READ
		response = webTarget.path("/" + report.getId()).request().get();
		
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		ReportDetailsResponseTESTDto foundReport = response.readEntity(ReportDetailsResponseTESTDto.class);
		assertNotNull(foundReport);
		assertEquals(report.getDescription(), foundReport.getData().getDescription());
		assertEquals(report.getName(), foundReport.getData().getName());
		assertEquals(report.getId(), foundReport.getData().getId());
		assertEquals(report.getVisibility(), foundReport.getData().getVisibility());
		
		response.close();
		
		//###################### TEST DELETE
		response = webTarget.path("/" + report.getId()).request().delete();
		
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		
		String resultMsg = response.readEntity(String.class);
		assertEquals("{\"code\":200}", resultMsg);
		
		response.close();
		
		//###################### TEST READ
		response = webTarget.path("/" + report.getId()).request().get();
		
		assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
		ReportResponseTESTDto errorResponse = response.readEntity(ReportResponseTESTDto.class);
		assertEquals(ErrorCodes.ENTRY_NOT_FOUND, errorResponse.getMsg());
		assertNull(errorResponse.getData());
		
		response.close();
		
	}
	
	
	@Test
	@Header(name="connection", value = "Keep-Alive")
	public void testDeleteNonExisting(@ArquillianResteasyResource("rest/report") ResteasyWebTarget webTarget) throws JsonParseException, JsonMappingException, IOException {
		
		//###################### TEST DELETE
		Response response = webTarget.path("/99999998" ).request().delete();
		
		assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
		
		ReportResponseTESTDto resultMsg = response.readEntity(ReportResponseTESTDto.class);
		assertEquals(ErrorCodes.DELETE_FAILED, resultMsg.getMsg());
		
		response.close();
		
	}
	

	@Test
//	@Header(name="connection", value = "Keep-Alive")
	public void testLogExecute(@ArquillianResteasyResource("rest/report") ResteasyWebTarget webTarget) throws JsonParseException, JsonMappingException, IOException {
		
		/*
		 * 1. list reports
		 * 2. keep the last execution datetime of the first report in the list
		 * 3. call the REST execute API with the ID of the particular report
		 * 4. List the reports again
		 * 5. find the same report 
		 * 6. compare if the datetime was modified to a later one
		 * */
		
		//1. 
		Collection<ReportDTO> reports = callListRESTAPI(webTarget, "123");
		assertFalse(reports.isEmpty());
		
		//2.
		ReportDTO fstReport = reports.iterator().next();
		
		//3.
		ReportResponseTESTDto responseExec = callExecuteRESTAPI(webTarget, fstReport.getId());
		
		//4.
		reports = callListRESTAPI(webTarget, "123");
		assertFalse(reports.isEmpty());
		
		//5.
		ReportDTO updatedReport = null;
		
		for (ReportDTO reportDTO : reports) {
			if (reportDTO.getId() == fstReport.getId()) {
				updatedReport = reportDTO;
				break;
			}
		}
		
		assertNotNull(updatedReport);

		//6.
		assertTrue(fstReport.getExecutionLogs().iterator().next().getExecutedOn().before(updatedReport.getExecutionLogs().iterator().next().getExecutedOn()));
	}
	
	
	private Collection<ReportDTO> callListRESTAPI(ResteasyWebTarget webTarget, String scopeName) throws JsonParseException, JsonMappingException, IOException {
		Response responseList = webTarget.path("/list").request().header(RestConstants.REQUEST_PARAM_CURRENT_USER_SCOPE, scopeName).get();
		
		assertNotNull(responseList);
		
		assertEquals(HttpServletResponse.SC_OK, responseList.getStatus());
		String foundReports = responseList.readEntity(String.class);
		assertNotNull(foundReports);
		
		ObjectMapper mapper = new ObjectMapper();  
		ReportResponseTESTDto responseDTO = mapper.readValue(foundReports, ReportResponseTESTDto.class);
		
		responseList.close();
		return responseDTO.getData();
	}
	
	private ReportResponseTESTDto callExecuteRESTAPI(ResteasyWebTarget webTarget, long reportId) throws JsonParseException, JsonMappingException, IOException {
		Response response = webTarget.path("/execute/"+reportId).request().get();
		
		assertNotNull(response);
		
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		ReportResponseTESTDto responseDto = response.readEntity(ReportResponseTESTDto.class);
		assertNotNull(responseDto);
		response.close();
		return responseDto;
	}
}

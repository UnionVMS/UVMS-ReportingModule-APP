package eu.europa.ec.fisheries.uvms.reporting.rest.resources;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.rest.constants.RestConstants;
import eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDetailsDTO;
import eu.europa.ec.fisheries.uvms.reporting.rest.util.ArquillianTest;
import eu.europa.ec.fisheries.uvms.reporting.rest.util.EntityUtil;
import eu.europa.ec.fisheries.uvms.rest.constants.ErrorCodes;
import eu.europa.ec.fisheries.uvms.rest.dto.ResponseDto;
import eu.europe.ec.fisheries.uvms.reporting.rest.service.dto.ReportDetailsResponseTESTDto;
import eu.europe.ec.fisheries.uvms.reporting.rest.service.dto.ReportResponseTESTDto;

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
	public void testUpdate(@ArquillianResteasyResource("rest/report") ResteasyWebTarget webTarget) throws JsonParseException, JsonMappingException, IOException {
		
		//check if we have the prerequisite - a report in the DB with ID = 1
		Response response = webTarget.path("/1" ).request().get();
		
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		ResponseDto responseDto = response.readEntity(ResponseDto.class);
		assertNotNull(responseDto);

		assertNotNull(responseDto.getData());
		
		response.close();
		
		
		// now let's try to update the entity
		long date = new Date().getTime();
		String payload = "{\"id\":1,"
				+ "\"name\":\"Report Name"+date+"\","
				+ "\"desc\":\"Some description"+date+"\","
				+ "\"visibility\":\"SCOPE\","
				+ "\"scopeId\":123,"
				+ "\"outComponents\":\"{\\\"map\\\":"+date+",\\\"vms\\\":true}\","
				+ "\"filterExpression\":\"{\\\"startDate\\\":\\\"2015-09-02 18:20:00\\\",\\\"endDate\\\":\\\"2015-09-02 18:20:00\\\",\\\"positionSelector\\\":\\\""+date+"\\\",\\\"vessels\\\":[{\\\"id\\\":1,\\\"name\\\":\\\"Vessel 1\\\",\\\"type\\\":\\\"vessel\\\"},{\\\"id\\\":2,\\\"name\\\":\\\"Vessel 2\\\",\\\"type\\\":\\\"vessel\\\"}],\\\"vms\\\":{\\\"positions\\\":{\\\"active\\\":false},\\\"segments\\\":{\\\"active\\\":false},\\\"tracks\\\":{\\\"active\\\":false}}}\"}";
		
		ObjectMapper mapper = new ObjectMapper();
		ReportDetailsDTO dto = mapper.readValue(payload, ReportDetailsDTO.class);
		
		assertNotNull(dto);
		assertNotNull(dto.getFilterExpression());
		assertNotNull(dto.getOutComponents());
		assertNotNull(dto.getVisibility());
		
		response = webTarget.path("/1" ).request(MediaType.APPLICATION_JSON).put(Entity.entity(dto,MediaType.APPLICATION_JSON));
		
		assertNotNull(response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		assertEquals("{\"code\":200}", response.readEntity(String.class));
		
		response.close();
		
		//and now let's verify if the update was properly persisted
		response = webTarget.path("/1" ).request().get();
		
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		ReportDetailsResponseTESTDto detailsResponseDTO = response.readEntity(ReportDetailsResponseTESTDto.class);
		assertNotNull(detailsResponseDTO);
		ReportDetailsDTO detailsDto = detailsResponseDTO.getData();
		assertEquals("{\"startDate\":\"2015-09-02 18:20:00\",\"endDate\":\"2015-09-02 18:20:00\",\"positionSelector\":\""+date+"\",\"vessels\":[{\"id\":1,\"name\":\"Vessel 1\",\"type\":\"vessel\"},{\"id\":2,\"name\":\"Vessel 2\",\"type\":\"vessel\"}],\"vms\":{\"positions\":{\"active\":false},\"segments\":{\"active\":false},\"tracks\":{\"active\":false}}}", detailsDto.getFilterExpression());
		assertEquals("Report Name"+date, detailsDto.getName());
		assertEquals("Some description"+date, detailsDto.getDesc());
		assertEquals(VisibilityEnum.SCOPE, detailsDto.getVisibility());
		assertEquals("{\"map\":"+date+",\"vms\":true}", detailsDto.getOutComponents());
		
		response.close();
		
		
		
	}
	

	@Test
	@Header(name="connection", value = "Keep-Alive")
	public void testCreateListReadDelete(@ArquillianResteasyResource("rest/report") ResteasyWebTarget webTarget) throws JsonParseException, JsonMappingException, IOException {
		
		//###################  START CREATE TEST
		ReportDetailsDTO reportDetailsDTO = EntityUtil.createRandomReportDetailsDTO();
		reportDetailsDTO.setVisibility(VisibilityEnum.SCOPE);
		
		Response response = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.json(reportDetailsDTO));
		
		assertNotNull(response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		assertEquals("{\"code\":200}", response.readEntity(String.class));
		
		response.close();
		
		//############ TEST LIST
		Response responseList = webTarget.path("/list").queryParam(RestConstants.REQUEST_PARAM_CURRENT_USER_SCOPE, reportDetailsDTO.getScopeId()).request().get();
		
		assertNotNull(responseList);
		
		assertEquals(HttpServletResponse.SC_OK, responseList.getStatus());
		String foundReports = responseList.readEntity(String.class);
		assertNotNull(foundReports);
		
		ObjectMapper mapper = new ObjectMapper();  
		ReportResponseTESTDto responseDTO = mapper.readValue(foundReports, ReportResponseTESTDto.class);
		
		Collection<ReportDTO> reports = responseDTO.getData();
		
		
		assertEquals(1, reports.size());
		
		ReportDTO report = reports.iterator().next();
		
		assertEquals(reportDetailsDTO.getDesc(), report.getDesc());
		assertEquals(reportDetailsDTO.getName(), report.getName());
		assertNotNull(report.getCreatedOn());
		assertTrue(report.getId()>0);
		assertTrue(report.isDeletable());
		assertTrue(report.isEditable());
		assertEquals(VisibilityEnum.SCOPE, report.getVisibility());
		assertTrue(report.isShareable());
		
		responseList.close();
		
		//###################### TEST READ
		response = webTarget.path("/" + report.getId()).request().get();
		
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		ReportDetailsResponseTESTDto foundReport = response.readEntity(ReportDetailsResponseTESTDto.class);
		assertNotNull(foundReport);
		assertEquals(report.getDesc(), foundReport.getData().getDesc());
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
		ResponseDto errorResponse = response.readEntity(ResponseDto.class);
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
		
		ResponseDto resultMsg = response.readEntity(ResponseDto.class);
		assertEquals(ErrorCodes.DELETE_FAILED, resultMsg.getMsg());
		
		response.close();
		
	}
	

}

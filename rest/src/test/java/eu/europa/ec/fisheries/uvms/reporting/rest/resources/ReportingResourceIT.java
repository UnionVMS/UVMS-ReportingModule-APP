/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.rest.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europa.ec.fisheries.uvms.constants.AuthConstants;
import eu.europa.ec.fisheries.uvms.reporting.dto.report.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDetailsResponseTESTDto;
import eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportResponseTESTDto;
import eu.europa.ec.fisheries.uvms.reporting.rest.util.ArquillianTest;
import eu.europa.ec.fisheries.uvms.reporting.rest.util.RestDTOUtil;
import eu.europa.ec.fisheries.uvms.reporting.dto.report.ReportDTO;
import eu.europa.ec.fisheries.uvms.rest.constants.ErrorCodes;
import eu.europa.ec.mare.usm.jwt.DefaultJwtTokenHandler;
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

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@RunAsClient
public class ReportingResourceIT extends ArquillianTest {
	
    @ArquillianResource
    URL contextPath;
	private String authToken;

    @Before
    public void before(){

    }

	@Before
	public void setUp() {
		DefaultJwtTokenHandler tokenHandler = new DefaultJwtTokenHandler();
		authToken = tokenHandler.createToken("rep_power");//rep_power has all features
	}

	@After
	public void tearDown() {

	}

	@Test
	@Header(name="connection", value = "Keep-Alive")
	public void testUpdate(@ArquillianResteasyResource("rest/report") ResteasyWebTarget webTarget) throws IOException {

        ReportDTO dto = callCreateRESTAPI(webTarget, "123");

        Response response = webTarget.path("/"+ dto.getId()).request().header(HttpHeaders.AUTHORIZATION, authToken)
                .header(AuthConstants.HTTP_HEADER_AUTHORIZATION, authToken).header(AuthConstants.HTTP_HEADER_SCOPE_NAME, "123").get();
        response.close();
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());

		// now let's try to update the entity
        dto.setName("changename");
		
		response = webTarget.path("/").request().header(HttpHeaders.AUTHORIZATION, authToken)
				.header(AuthConstants.HTTP_HEADER_AUTHORIZATION, authToken).header(AuthConstants.HTTP_HEADER_SCOPE_NAME,
                        dto.getScopeName()).put(Entity.json(dto));

        assertNotNull(response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		assertEquals("{\"code\":200}", response.readEntity(String.class));
        response.close();

		//and now let's verify if the update was properly persisted
		response = webTarget.path("/"+ dto.getId()).request().get();
		
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());

        ReportDTO updated = mapReportDTO(response);

        assertEquals(updated.getName(), dto.getName());

        response.close();
	}

    ReportDTO mapReportDTO(Response response) throws IOException {
		assert response.getStatus() == HttpServletResponse.SC_OK;

        ObjectMapper mapper = new ObjectMapper();
        String s = response.readEntity(String.class);
        String replace = s.replace("{\"data\":", "");
        String substring = replace.substring(0, replace.length() - 1);
        return mapper.readValue(substring, ReportDTO.class);
    }

	@Test
	@Header(name="connection", value = "Keep-Alive")
	public void testCreateListReadDelete(@ArquillianResteasyResource("rest/report") ResteasyWebTarget webTarget) throws IOException {

		//FIXME the problem comes from the non-symmetric Serializer/deserializer
        //###################  START CREATE TEST

		ReportDTO resultReport = callCreateRESTAPI( webTarget, "CreateListReadDelete");

		//############ TEST LIST

        Collection<ReportDTO> reports = callListRESTAPI(webTarget, resultReport.getScopeName());

		assertEquals(1, reports.size());
		
		ReportDTO report = reports.iterator().next();
		
		assertEquals(resultReport.getDescription(), report.getDescription());
		assertEquals(resultReport.getName(), report.getName());
		assertNotNull(report.getAudit().getCreatedOn());
		assertTrue(report.getId()>0);
		assertTrue(report.isDeletable());
		assertTrue(report.isEditable());
		assertEquals(VisibilityEnum.SCOPE, report.getVisibility());
		//assertTrue(report.isShareable()); TODO add check
		
		//###################### TEST READ
		Response response = webTarget.path("/" + report.getId()).request().get();
		
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		ReportDetailsResponseTESTDto foundReport = response.readEntity(ReportDetailsResponseTESTDto.class);
		assertNotNull(foundReport);
		assertEquals(report.getDescription(), foundReport.getData().getDescription());
		assertEquals(report.getName(), foundReport.getData().getName());
		ReportDTO reportDTO = reports.iterator().next();
		
		assertEquals(reportDTO.getDescription(), resultReport.getDescription());
		assertEquals(reportDTO.getName(), resultReport.getName());
		assertNotNull(reportDTO.getAudit());
		assertTrue(reportDTO.getId()>0);
		assertTrue(reportDTO.isDeletable());
		assertTrue(reportDTO.isEditable());
		assertEquals(VisibilityEnum.SCOPE, reportDTO.getVisibility());
		//assertTrue(reportDTO.isShareable()); TODO add check
		
		//###################### TEST READ
		response = webTarget.path("/" + reportDTO.getId()).request().get();
		
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		ReportDetailsResponseTESTDto testDto = response.readEntity(ReportDetailsResponseTESTDto.class);
		assertNotNull(testDto);
		assertEquals(reportDTO.getDescription(), testDto.getData().getDescription());
		assertEquals(reportDTO.getName(), testDto.getData().getName());
		assertEquals(reportDTO.getId(), testDto.getData().getId());
		assertEquals(reportDTO.getId(), testDto.getData().getId(), 0.01);
		assertEquals(reportDTO.getVisibility(), testDto.getData().getVisibility());
		
		response.close();
		
		//###################### TEST DELETE
		response = webTarget.path("/" + reportDTO.getId()).request().delete();
		
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		
		String resultMsg = response.readEntity(String.class);
		assertEquals("{\"code\":200}", resultMsg);
		
		response.close();
		
		//###################### TEST READ
		response = webTarget.path("/" + reportDTO.getId()).request().get();
		
		assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
		ReportResponseTESTDto errorResponse = response.readEntity(ReportResponseTESTDto.class);
		assertEquals(ErrorCodes.ENTRY_NOT_FOUND, errorResponse.getMsg());
		assertNull(errorResponse.getData());
		
		response.close();

        Response delete = webTarget.path("/" + report.getId()).request().header(AuthConstants.HTTP_HEADER_SCOPE_NAME,
                report.getScopeName()).delete();
            delete.close();
	}

	private ReportDTO callCreateRESTAPI(ReportDTO dto, ResteasyWebTarget webTarget, String scopeName) throws IOException {

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(dto);

		Response response = webTarget.request(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, authToken)
				.header(AuthConstants.HTTP_HEADER_SCOPE_NAME, scopeName)
				.post(Entity.text(jsonString));
		ReportDTO reportDTO = mapReportDTO(response);
		response.close();
		return reportDTO;
	}

	@Test
	@Header(name="connection", value = "Keep-Alive")
	public void testDeleteNonExisting(@ArquillianResteasyResource("rest/report") ResteasyWebTarget webTarget) throws IOException {
		
		//###################### TEST DELETE
		Response response = webTarget.path("/99999998" ).request().delete();
		
		assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
		
		ReportResponseTESTDto resultMsg = response.readEntity(ReportResponseTESTDto.class);
		assertEquals(ErrorCodes.DELETE_FAILED, resultMsg.getMsg());
		
		response.close();
		
	}

	private ReportDTO callCreateRESTAPI(ResteasyWebTarget webTarget, String scopeName) throws IOException {
        ReportDTO dto = RestDTOUtil.createRandomReport();
		dto.setVisibility(VisibilityEnum.SCOPE);

       return callCreateRESTAPI(dto, webTarget, scopeName);
    }

	private Collection<ReportDTO> callListRESTAPI(ResteasyWebTarget webTarget, String scopeName) throws IOException {
		Response responseList = webTarget.path("/list").request().header(AuthConstants.HTTP_HEADER_SCOPE_NAME, scopeName).get();
		
		assertNotNull(responseList);
		
		assertEquals(HttpServletResponse.SC_OK, responseList.getStatus());
		String foundReports = responseList.readEntity(String.class);
		assertNotNull(foundReports);

        String replace = foundReports.replace("{\"data\":", "");
        String substring = replace.substring(0, replace.length() - 1);

		ObjectMapper mapper = new ObjectMapper();
        List<ReportDTO> reportDTOs = Arrays.asList(mapper.readValue(substring, ReportDTO[].class));

		responseList.close();
		return reportDTOs;
	}


}
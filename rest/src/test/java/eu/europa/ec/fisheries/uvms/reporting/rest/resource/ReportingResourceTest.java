/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.reporting.rest.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europa.ec.fisheries.schema.movement.v1.MovementActivityTypeType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSourceType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.schema.movement.v1.SegmentCategoryType;
import eu.europa.ec.fisheries.uvms.reporting.rest.resources.ReportingResource;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.ReportExecutionService;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.impl.ReportServiceBean;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.CommonFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FaFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.PositionSelectorDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.TrackFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsPositionFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsSegmentFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.report.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.report.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Selector;
import eu.europa.ec.fisheries.uvms.reporting.service.enums.ReportTypeEnum;
import eu.europa.ec.fisheries.uvms.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.rest.security.bean.USMService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ReportingResourceTest extends JerseyTest {

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private ReportServiceBean reportService;

    @Mock
    private USMService usmService;

    @Mock
    private ReportExecutionService reportExecutionService;

    private ReportDTO reportDto;

    @Override
    protected Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        MockitoAnnotations.initMocks(this);
        ResourceConfig config = new ResourceConfig();
        ReportingResource reportingResource = new ReportingResource();
        setInternalState(reportingResource, "reportService", reportService);
        setInternalState(reportingResource, "reportExecutionService", reportExecutionService);
        setInternalState(reportingResource, "usmService", usmService);
        setInternalState(reportingResource, "applicationName", "reporting");

        config.register(reportingResource);

        config.register(DebugMapper.class);
        config.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(httpServletRequest).to(HttpServletRequest.class);
            }
        });
        return config;
    }

    @Before
    public void before() {
        reportDto = new ReportDTO();
        reportDto.setId(1L);
        reportDto.setDescription("description");
        reportDto.setName("name");
        reportDto.setCreatedBy("createdBy");

        CommonFilterDTO common = new CommonFilterDTO();
        common.setStartDate(new Date());
        common.setEndDate(new Date());
        common.setId(1L);
        PositionSelectorDTO positionSelector = new PositionSelectorDTO();
        positionSelector.setSelector(Selector.all);
        common.setPositionSelector(positionSelector);

        TrackFilterDTO track = new TrackFilterDTO();
        track.setId(1L);
        track.setMaxAvgSpeed(10F);
        track.setMinAvgSpeed(20F);
        track.setMaxDistance(1000F);
        track.setMinDistance(100F);
        track.setMaxDuration(1000F);
        track.setMinDuration(100F);
        track.setMinTime(12F);
        track.setMaxTime(120F);
        reportDto.addFilter(track);

        VmsPositionFilterDTO position = new VmsPositionFilterDTO();
        position.setId(1L);
        position.setMinimumSpeed(10F);
        position.setMaximumSpeed(200F);
        position.setMovementActivity(MovementActivityTypeType.ANC);
        position.setMovementType(MovementTypeType.ENT);
        position.setMovementSources(Collections.singletonList(MovementSourceType.INMARSAT_C.value()));
        reportDto.addFilter(position);

        VmsSegmentFilterDTO segment = new VmsSegmentFilterDTO();
        segment.setId(1L);
        segment.setMinimumSpeed(11F);
        segment.setMaximumSpeed(50F);
        segment.setCategory(SegmentCategoryType.ANCHORED);
        segment.setMinDuration(1F);
        segment.setMaxDuration(20F);
        reportDto.addFilter(segment);

        FaFilterDTO activity = new FaFilterDTO();
        activity.setId(1L);
        activity.setReportId(1L);
        activity.setActivityTypes(Arrays.asList("type1", "type2"));
        reportDto.addFilter(activity); // TODO

        assertNotNull(reportDto.getAudit().getCreatedOn());
        assertEquals(ReportTypeEnum.STANDARD, reportDto.getReportTypeEnum());
        assertEquals(VisibilityEnum.PRIVATE, reportDto.getVisibility());
        assertEquals(false, reportDto.isDefault());
        assertEquals(false, reportDto.isDeletable());
        assertEquals(false, reportDto.isDeleted());
        assertEquals(true, reportDto.isEditable());
        assertEquals(null, reportDto.getExecutionLog());
        assertEquals(true, reportDto.getWithMap());

        reportDto.addFilter(common);

    }

    @Test
    public void test() {

        when(reportService.findById(anySetOf(String.class), anyLong(), anyString(), anyString(), anyBoolean(), anyListOf(String.class))).thenReturn(reportDto);
        when(httpServletRequest.getRemoteUser()).thenReturn(any(String.class));

        Client client = new JerseyClientBuilder().build();
        WebTarget target = client.target("http://localhost:9998");
        Response response = target.path("report/1").request().get();

        ResponseDto responseDto = response.readEntity(ResponseDto.class);
        Object data = responseDto.getData();
        ObjectMapper mapper = new ObjectMapper();
        ReportDTO reportResponse = mapper.convertValue(data, ReportDTO.class);

        assertEquals(reportResponse, reportDto);

        List<FilterDTO> filters = reportResponse.getFilters();
        for (FilterDTO filterDTO : filters) {
            assertTrue(filters.contains(filterDTO));
        }

    }

    @Provider
    public static class DebugMapper implements ExceptionMapper<Throwable> {

        @Override
        public Response toResponse(Throwable e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
}

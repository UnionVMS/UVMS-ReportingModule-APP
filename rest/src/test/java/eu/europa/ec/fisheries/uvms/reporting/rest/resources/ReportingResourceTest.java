package eu.europa.ec.fisheries.uvms.reporting.rest.resources;

import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.ReportServiceBean;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.VmsService;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsDTO;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;
import org.unitils.mock.annotation.Dummy;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ReportingResourceTest extends UnitilsJUnit4 {

    private static final String USER = "georgi";

    @TestedObject
    private ReportingResource resource;

    @InjectIntoByType
    private Mock<VmsService> vmsServiceMock;

    @InjectIntoByType
    private Mock<ReportServiceBean> reportServiceBeanMock;

    private Mock<HttpServletRequest> requestMock;

    @Dummy
    private MovementMapResponseType responseType;

    @Dummy
    private VmsDTO vmsDTO;

    @Test
    public void testRunReportHappy() throws IOException, ReportingServiceException {

        requestMock.returns(USER).getRemoteUser();
        vmsServiceMock.returns(vmsDTO).getVmsDataByReportId(USER, null, null);

        Response response = resource.runReport(requestMock.getMock(), null, null, null);

        assertEquals(200, response.getStatus());

    }

    @Test
    public void testRunReport500() throws IOException, ReportingServiceException {

        requestMock.returns(USER).getRemoteUser();
        vmsServiceMock.onceRaises(ReportingServiceException.class).getVmsDataByReportId(USER, null, null);

        Response response = resource.runReport(requestMock.getMock(), null, null, null);

        assertEquals(500, response.getStatus());

    }
}

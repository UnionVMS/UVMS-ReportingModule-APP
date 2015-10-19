package eu.europa.ec.fisheries.uvms.reporting.rest.resources;

import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.uvms.constants.AuthConstants;
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
import java.util.ArrayList;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;

//TODO implement unit testing for all the error handlings
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

    @Test
    public void testListReports200() throws IOException, ReportingServiceException {

        requestMock.returns(new TreeSet<String>()).getServletContext().getAttribute(AuthConstants.HTTP_SERVLET_CONTEXT_ATTR_FEATURES);
        reportServiceBeanMock.returns(new ArrayList<ReportDTO>()).listByUsernameAndScope(null, null, null);

        Response response = resource.listReports(requestMock.getMock(), null, null);

        assertEquals(200, response.getStatus());

    }

    @Test
    public void testListReports500() throws IOException, ReportingServiceException {

        requestMock.returns(null).getServletContext().getAttribute(AuthConstants.HTTP_SERVLET_CONTEXT_ATTR_FEATURES);

        Response response = resource.listReports(requestMock.getMock(), null, null);

        assertEquals(500, response.getStatus());

    }

}

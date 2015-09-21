package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingModelMarshallException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterExpression;
import junit.framework.TestCase;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static junit.framework.TestCase.assertEquals;

public class ReportingJSONMarshallerTest {

    private ReportingJSONMarshaller marshaller;

    @Before
    public void init(){
        marshaller = new ReportingJSONMarshaller();
    }

    @Test
    public void testMarshall() throws IOException, ReportingModelMarshallException {
        URL url = Resources.getResource("testMarshallJsonStringToObject.json");
        String json = Resources.toString(url, Charsets.UTF_8);
        FilterExpression filter = marshaller.marshall(json, FilterExpression.class);
        assertEquals("2015-08-31 15:45:00", DateUtils.FILTER_FORMATTER.print(new DateTime(filter.getStartDate(), DateTimeZone.UTC)));
        assertEquals("2015-08-31 15:50:00", DateUtils.FILTER_FORMATTER.print(new DateTime(filter.getEndDate(), DateTimeZone.UTC)));
        TestCase.assertEquals("Vessel 1", filter.getVessels().get(0).getName());
        TestCase.assertEquals("34252bbc-03bf-9c30-5121-de7d3cfccde0", filter.getVessels().get(0).getGuid());
        TestCase.assertEquals("vessel", filter.getVessels().get(0).getType().getName());
        TestCase.assertEquals("Vessel 2", filter.getVessels().get(1).getName());
        TestCase.assertEquals("f0938867-b8fe-f707-7a3e-5117bbb7c40a", filter.getVessels().get(1).getGuid());
        TestCase.assertEquals("vessel", filter.getVessels().get(1).getType().getName());
        TestCase.assertEquals("all", filter.getPositionSelector().getName());
        TestCase.assertEquals(false, filter.getVms().getPositions().getActive());
        TestCase.assertEquals(false, filter.getVms().getSegments().getActive());
        TestCase.assertEquals(false, filter.getVms().getTracks().getActive());
    }
}

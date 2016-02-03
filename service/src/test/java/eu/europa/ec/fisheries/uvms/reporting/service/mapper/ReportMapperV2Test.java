package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.vividsolutions.jts.util.Assert;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ExecutionLog;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportDetails;
import lombok.SneakyThrows;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.HashSet;
import java.util.Set;

public class ReportMapperV2Test {

    private static ObjectMapper mapper;

    @BeforeClass
    public static void beforeClass(){
        mapper = new ObjectMapper();
    }

    @Test
    @SneakyThrows
    public void testReportDtoToReport(){

        String expectedJSONString = Resources.toString(Resources.getResource("payloads/ReportMapperV2Test.json"), Charsets.UTF_8);

        Report report = ReportMapperV2.INSTANCE.reportDtoToReport(mapper.readValue(expectedJSONString, eu.europa.ec.fisheries.uvms.reporting.model.vms.Report.class));

        assertNotNull(report);

        Set<Filter> filters = report.getFilters();

        assertEquals(9, filters.size());

    }
}

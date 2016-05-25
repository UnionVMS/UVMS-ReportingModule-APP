package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ExecutionLog;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportDetails;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

import java.util.ArrayList;
import java.util.HashSet;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;

//TODO implement more tests
public class ReportMapperTest extends UnitilsJUnit4 {

    @TestedObject
    private ReportMapper mapper = ReportMapper.ReportMapperBuilder().filters(false).build();

    private Report report;

    @Before
    public void before(){
        report = Report.builder().createdBy("you").details(
                ReportDetails.builder().description("desc").scopeName("scopeName").name("name").withMap(true).build())
                .executionLogs(new HashSet<ExecutionLog>())
                .build();
    }

    @Test
    public void testReportDTOToReportWithNull(){
        Report report = mapper.reportDTOToReport(null);

        assertNull(report);
    }

    @Test
    public void testReportToReportDTOWithNoFilters(){

        ReportDTO expectedDTO = ReportDTO.ReportDTOBuilder()
                .id(1L)
                .createdBy("you")
                .description("desc")
                .name("name")
                .withMap(true)
                .visibility(VisibilityEnum.PRIVATE)
                .scopeName("scopeName")
                .filters(new ArrayList<FilterDTO>())
                .build();

        ReportDTO dto = mapper.reportToReportDTO(report);

        assertTrue(expectedDTO.equals(dto));

    }

    @Test
    public void testReportToReportDTOWithReportNull(){

        ReportDTO dto = mapper.reportToReportDTO(null);

        assertNull(dto);

    }
}

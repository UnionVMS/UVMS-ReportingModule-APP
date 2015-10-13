package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

import static org.junit.Assert.assertEquals;

public class ReportMapperTest extends UnitilsJUnit4 {

    @TestedObject
    private ReportMapper mapper;

    private Report report;

    @Before
    public void before(){
        report = Report.ReportBuilder().createdBy("you").description("desc").id(1L).name("name")
                .withMap(true).scopeName("scopeName").build();
    }

    @Test
    @Ignore
    public void testReportToReportDTO(){

        ReportDTO expectedDTO = ReportDTO.ReportDTOBuilder()
                .id(1L)
                .name("name")
                .description("desc")
                .scopeName("scopeName")
                .createdBy("you")
                .withMap(true)
                .build();

        ReportDTO dto = mapper.reportToReportDTO(report);

        assertEquals(expectedDTO, dto);

    }
}

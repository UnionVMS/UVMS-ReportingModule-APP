package eu.europa.ec.fisheries.uvms.reporting.rest.mapper;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.europa.ec.fisheries.uvms.reporting.model.Report;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDetailsDTO;
import eu.europa.ec.fisheries.uvms.reporting.rest.util.EntityUtil;

public class ReportDetailsDTOToReportMapperTest {
	
	private ReportDetailsDTOToReportMapper mapper = ReportDetailsDTOToReportMapper.INSTANCE; 

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testReportDetailsDtoToReport() {
		ReportDetailsDTO detailsDTO = EntityUtil.createRandomReportDetailsDTO();
		detailsDTO.setDesc("56385673456342614252345");
		detailsDTO.setId(7345334);
		detailsDTO.setVisibility(VisibilityEnum.SCOPE);
		detailsDTO.setName("234565645235456754");
		detailsDTO.setOutComponents("1453452365376523423");
		detailsDTO.setScopeId(543535);
		
		Report report = mapper.reportDetailsDtoToReport(detailsDTO);
		
		assertEquals(detailsDTO.getDesc(), report.getDescription());
		assertEquals(detailsDTO.getId(), report.getId());
		assertEquals(detailsDTO.getName(), report.getName());
		assertEquals(detailsDTO.getOutComponents(), report.getOutComponents());
		assertEquals(detailsDTO.getScopeId(), report.getScopeId());
		assertEquals(detailsDTO.getVisibility(), report.getVisibility());
	}

	@Test
	public void testReportToReportDetailsDto() {
		Report report = EntityUtil.createRandomReport();
		report.setDescription("56385673456342614252345");
		report.setId(7345334);
		report.setVisibility(VisibilityEnum.SCOPE);
		report.setName("234565645235456754");
		report.setOutComponents("1453452365376523423");
		report.setScopeId(543535);
		
		ReportDetailsDTO detailsDTO = mapper.reportToReportDetailsDto(report);
		
		assertEquals(report.getDescription(), detailsDTO.getDesc());
		assertEquals(report.getId(), detailsDTO.getId());
		assertEquals(report.getName(), detailsDTO.getName());
		assertEquals(report.getOutComponents(), detailsDTO.getOutComponents());
		assertEquals(report.getScopeId(), detailsDTO.getScopeId());
		assertEquals(report.getVisibility(), detailsDTO.getVisibility());
	}

}

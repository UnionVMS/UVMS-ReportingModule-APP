package eu.europa.ec.fisheries.uvms.reporting.rest.mapper;

import static org.junit.Assert.*;

import java.util.Date;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.ExecutionLogDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.europa.ec.fisheries.uvms.reporting.model.Context;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.model.ReportFeature;
import eu.europa.ec.fisheries.uvms.reporting.rest.temp.MockingUtils;
import eu.europa.ec.fisheries.uvms.reporting.rest.util.EntityUtil;

public class ReportToDTOMapperTest {
	
	private ReportToDTOMapper mapper = ReportToDTOMapper.INSTANCE;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testLastExecDate () {
		ReportDTO report = EntityUtil.createRandomReport();
		ExecutionLogDTO log = new ExecutionLogDTO();
		log.setExecutedBy("zsdfgasdfdasf");
		log.setExecutedOn(new Date());
		report.getExecutionLogs().add(log);
		
		eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDTO dto = mapper.reportToReportDto(report);
		assertNotNull(dto);
		assertEquals(log.getExecutedOn(), dto.getLastExecTime());
	}
	
	@Test
	public void testNoLastExecDate () {
		ReportDTO report = EntityUtil.createRandomReport();
		
		eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDTO dto = mapper.reportToReportDto(report);
		assertNotNull(dto);
		assertNull(dto.getLastExecTime());
	}

	@Test
	public void testShareable() {
		ReportDTO report = EntityUtil.createRandomReport();
		report.setCreatedBy("georgi");
		Context  context = MockingUtils.createContext("someScope", ReportFeature.SHARE_REPORTS_GLOBAL);
		eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDTO reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertTrue(reportDTO.isShareable());
		
		report = EntityUtil.createRandomReport();
		report.setCreatedBy("georgi");
		context = MockingUtils.createContext("someScope", ReportFeature.SHARE_REPORTS_SCOPE);
		reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertTrue(reportDTO.isShareable());
		assertEquals(report.getCreatedBy(), reportDTO.getCreatedBy());
	}
	
	@Test
	public void testShareableNegative() {
		ReportDTO report = EntityUtil.createRandomReport();
		report.setCreatedBy("hugo");
		Context  context = MockingUtils.createContext("someScope", ReportFeature.SHARE_REPORTS_SCOPE);
		eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDTO reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertFalse(reportDTO.isShareable());
		assertEquals(report.getCreatedBy(), reportDTO.getCreatedBy());
		
	}
	
	@Test
	public void testEditable() {
		ReportDTO report = EntityUtil.createRandomReport();
		report.setCreatedBy("georgi");
		Context  context = MockingUtils.createContext("someScope", ReportFeature.MODIFY_PRIVATE_REPORT);
		eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDTO reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertTrue(reportDTO.isEditable());
		assertEquals(report.getCreatedBy(), reportDTO.getCreatedBy());
		
		//if a user is allowed to edit shared reports
		report = EntityUtil.createRandomReport();
		report.setCreatedBy("hugo");
		report.setVisibility(VisibilityEnum.SCOPE);
		context = MockingUtils.createContext("someScope", ReportFeature.MODIFY_SCOPE_REPORT);
		reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertTrue(reportDTO.isEditable());
		
		report = EntityUtil.createRandomReport();
		report.setCreatedBy("hugo");
		report.setVisibility(VisibilityEnum.PUBLIC);
		context = MockingUtils.createContext("someScope", ReportFeature.MODIFY_GLOBAL_REPORT);
		reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertTrue(reportDTO.isEditable());
		assertEquals(report.getCreatedBy(), reportDTO.getCreatedBy());
	}
	
	@Test
	public void testEditableNegative() {
		ReportDTO report = EntityUtil.createRandomReport();
		report.setCreatedBy("georgi");
		Context  context = MockingUtils.createContext("someScope");
		eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDTO reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertFalse(reportDTO.isEditable());
		
		report = EntityUtil.createRandomReport();
		report.setCreatedBy("georgi");
		context = MockingUtils.createContext("someScope", ReportFeature.MODIFY_SCOPE_REPORT);
		reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertFalse(reportDTO.isEditable());
		
		//if a user is allowed to edit shared reports
		report = EntityUtil.createRandomReport();
		report.setCreatedBy("hugo");
		report.setVisibility(VisibilityEnum.SCOPE);
		context = MockingUtils.createContext("someScope", ReportFeature.MODIFY_PRIVATE_REPORT);
		reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertFalse(reportDTO.isEditable());
		
		//report that is not shared
		report = EntityUtil.createRandomReport();
		report.setCreatedBy("hugo");
		report.setVisibility(VisibilityEnum.PRIVATE);
		context = MockingUtils.createContext("someScope", ReportFeature.MODIFY_SCOPE_REPORT);
		reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertFalse(reportDTO.isEditable());
		
		
		report = EntityUtil.createRandomReport();
		report.setCreatedBy("hugo");
		report.setVisibility(VisibilityEnum.PUBLIC);
		context = MockingUtils.createContext("someScope", ReportFeature.MODIFY_SCOPE_REPORT);
		reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertFalse(reportDTO.isEditable());
	}
	
	@Test
	public void testDeletable() {
		ReportDTO report = EntityUtil.createRandomReport();
		report.setCreatedBy("georgi");
		Context  context = MockingUtils.createContext("someScope", ReportFeature.DELETE_REPORT);
		eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDTO reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertTrue(reportDTO.isDeletable());
		
		//if a user is allowed to edit shared reports
		report = EntityUtil.createRandomReport();
		report.setCreatedBy("hugo");
		report.setVisibility(VisibilityEnum.SCOPE);
		context = MockingUtils.createContext("someScope", ReportFeature.DELETE_SCOPE_REPORT);
		reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertTrue(reportDTO.isDeletable());
		
		
		report = EntityUtil.createRandomReport();
		report.setCreatedBy("hugo");
		report.setVisibility(VisibilityEnum.PUBLIC);
		context = MockingUtils.createContext("someScope", ReportFeature.DELETE_GLOBAL_REPORT);
		reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertTrue(reportDTO.isDeletable());
	}
	
	@Test
	public void testDeletableNegative() {
		ReportDTO report = EntityUtil.createRandomReport();
		report.setCreatedBy("georgi");
		Context  context = MockingUtils.createContext("someScope");
		eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDTO reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertFalse(reportDTO.isDeletable());
		
		report = EntityUtil.createRandomReport();
		report.setCreatedBy("hugo");
		report.setVisibility(VisibilityEnum.PRIVATE);
		context = MockingUtils.createContext("someScope", ReportFeature.DELETE_SCOPE_REPORT);
		reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertFalse(reportDTO.isDeletable());
		
		report = EntityUtil.createRandomReport();
		report.setCreatedBy("hugo");
		report.setVisibility(VisibilityEnum.SCOPE);
		context = MockingUtils.createContext("someScope");
		reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertFalse(reportDTO.isDeletable());
		
		report = EntityUtil.createRandomReport();
		report.setCreatedBy("hugo");
		report.setVisibility(VisibilityEnum.SCOPE);
		context = MockingUtils.createContext("someScope", ReportFeature.DELETE_REPORT);
		reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertFalse(reportDTO.isDeletable());
		
		report = EntityUtil.createRandomReport();
		report.setCreatedBy("hugo");
		report.setVisibility(VisibilityEnum.PUBLIC);
		context = MockingUtils.createContext("someScope", ReportFeature.DELETE_REPORT);
		reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertFalse(reportDTO.isDeletable());
		
		
		report = EntityUtil.createRandomReport();
		report.setCreatedBy("hugo");
		report.setVisibility(VisibilityEnum.PUBLIC);
		context = MockingUtils.createContext("someScope", ReportFeature.DELETE_SCOPE_REPORT);
		reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertFalse(reportDTO.isDeletable());
	}

}

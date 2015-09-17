package eu.europa.ec.fisheries.uvms.reporting.rest.mapper;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.europa.ec.fisheries.uvms.reporting.model.Context;
import eu.europa.ec.fisheries.uvms.reporting.model.Report;
import eu.europa.ec.fisheries.uvms.reporting.model.ReportExecutionLog;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.rest.constants.ReportFeature;
import eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDTO;
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
		Report report = EntityUtil.createRandomReport();
		ReportExecutionLog log = new ReportExecutionLog();
		log.setExecutedBy("zsdfgasdfdasf");
		log.setExecutedOn(new Date());
		report.getReportExecutionLogs().add(log);
		
		ReportDTO dto = mapper.reportToReportDto(report);
		assertNotNull(dto);
		assertEquals(log.getExecutedOn(), dto.getLastExecTime());
	}
	
	@Test
	public void testNoLastExecDate () {
		Report report = EntityUtil.createRandomReport();
		
		ReportDTO dto = mapper.reportToReportDto(report);
		assertNotNull(dto);
		assertNull(dto.getLastExecTime());
	}

	@Test
	public void testShareable() {
		Report report = EntityUtil.createRandomReport();
		report.setCreatedBy("georgi");
		Context  context = MockingUtils.createContext("someScope", ReportFeature.SHARE_REPORTS_GLOBAL);
		ReportDTO reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertTrue(reportDTO.isShareable());
		
		report = EntityUtil.createRandomReport();
		report.setCreatedBy("georgi");
		context = MockingUtils.createContext("someScope", ReportFeature.SHARE_REPORTS_SCOPE);
		reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertTrue(reportDTO.isShareable());
		assertEquals(report.getCreatedOn(), reportDTO.getCreatedOn());
		assertEquals(report.getCreatedBy(), reportDTO.getCreatedBy());
	}
	
	@Test
	public void testShareableNegative() {
		Report report = EntityUtil.createRandomReport();
		report.setCreatedBy("hugo");
		Context  context = MockingUtils.createContext("someScope", ReportFeature.SHARE_REPORTS_SCOPE);
		ReportDTO reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertFalse(reportDTO.isShareable());
		assertEquals(report.getCreatedOn(), reportDTO.getCreatedOn());
		assertEquals(report.getCreatedBy(), reportDTO.getCreatedBy());
		
	}
	
	@Test
	public void testEditable() {
		Report report = EntityUtil.createRandomReport();
		report.setCreatedBy("georgi");
		Context  context = MockingUtils.createContext("someScope", ReportFeature.MODIFY_PRIVATE_REPORT);
		ReportDTO reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertTrue(reportDTO.isEditable());
		assertEquals(report.getCreatedOn(), reportDTO.getCreatedOn());
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
		assertEquals(report.getCreatedOn(), reportDTO.getCreatedOn());
		assertEquals(report.getCreatedBy(), reportDTO.getCreatedBy());
	}
	
	@Test
	public void testEditableNegative() {
		Report report = EntityUtil.createRandomReport();
		report.setCreatedBy("georgi");
		Context  context = MockingUtils.createContext("someScope");
		ReportDTO reportDTO = mapper.reportToReportDto(report, "georgi",  context);
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
		Report report = EntityUtil.createRandomReport();
		report.setCreatedBy("georgi");
		Context  context = MockingUtils.createContext("someScope", ReportFeature.DELETE_REPORT);
		ReportDTO reportDTO = mapper.reportToReportDto(report, "georgi",  context);
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
		Report report = EntityUtil.createRandomReport();
		report.setCreatedBy("georgi");
		Context  context = MockingUtils.createContext("someScope");
		ReportDTO reportDTO = mapper.reportToReportDto(report, "georgi",  context);
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

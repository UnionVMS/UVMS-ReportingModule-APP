package eu.europa.ec.fisheries.uvms.reporting.rest.mapper;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.europa.ec.fisheries.uvms.reporting.model.Context;
import eu.europa.ec.fisheries.uvms.reporting.model.Report;
import eu.europa.ec.fisheries.uvms.reporting.rest.constants.ReportFeature;
import eu.europa.ec.fisheries.uvms.reporting.rest.constants.RestConstants;
import eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.rest.temp.MockingUtils;
import eu.europa.ec.fisheries.uvms.reporting.rest.util.EntityUtil;

public class ReportToDTOMapperTest {
	
	private ReportToDTOMapper mapper;

	@Before
	public void setUp() throws Exception {
		mapper = new ReportToDTOMapper() {
			
			@Override
			public Collection<ReportDTO> reportsToReportDtos(Collection<Report> reports) {
				Collection<ReportDTO> collection = new ArrayList<>(reports.size());
				for (Report report : reports) {
					collection.add(this.reportToReportDto(report));
				}
				return collection;
			}
			
			@Override
			public ReportDTO reportToReportDto(Report report) {
				return new ReportDTO();
			}
			
			@Override
			public Collection<Report> reportDtosToReports(Collection<ReportDTO> reportDTOs) {
				Collection<Report> collection = new ArrayList<>(reportDTOs.size());
				for (ReportDTO report : reportDTOs) {
					collection.add(this.reportDtoToReport(report));
				}
				return collection;
			}
			
			@Override
			public Report reportDtoToReport(ReportDTO report) {
				return new Report();
			}
		};
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testShareable() {
		Report report = EntityUtil.createRandomReport();
		report.setCreatedBy("georgi");
		Context  context = MockingUtils.createContext("someScope", ReportFeature.SHARE_REPORTS);
		ReportDTO reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertTrue(reportDTO.isShareable());
	}
	
	@Test
	public void testShareableNegative() {
		Report report = EntityUtil.createRandomReport();
		report.setCreatedBy("hugo");
		Context  context = MockingUtils.createContext("someScope", ReportFeature.SHARE_REPORTS);
		ReportDTO reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertFalse(reportDTO.isShareable());
	}
	
	@Test
	public void testEditable() {
		Report report = EntityUtil.createRandomReport();
		report.setCreatedBy("georgi");
		Context  context = MockingUtils.createContext("someScope", ReportFeature.MODIFY_REPORT);
		ReportDTO reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertTrue(reportDTO.isEditable());
		
		//if a user is allowed to edit shared reports
		report = EntityUtil.createRandomReport();
		report.setCreatedBy("hugo");
		report.setIsShared(true);
		context = MockingUtils.createContext("someScope", ReportFeature.MODIFY_SHARED_REPORTS);
		reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertTrue(reportDTO.isEditable());
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
		context = MockingUtils.createContext("someScope", ReportFeature.MODIFY_SHARED_REPORTS);
		reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertFalse(reportDTO.isEditable());
		
		//if a user is allowed to edit shared reports
		report = EntityUtil.createRandomReport();
		report.setCreatedBy("hugo");
		report.setIsShared(true);
		context = MockingUtils.createContext("someScope", ReportFeature.MODIFY_REPORT);
		reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertFalse(reportDTO.isEditable());
		
		//report that is not shared
		report = EntityUtil.createRandomReport();
		report.setCreatedBy("hugo");
		report.setIsShared(false);
		context = MockingUtils.createContext("someScope", ReportFeature.MODIFY_SHARED_REPORTS);
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
		report.setIsShared(true);
		context = MockingUtils.createContext("someScope", ReportFeature.DELETE_SHARED_REPORT);
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
		report.setIsShared(false);
		context = MockingUtils.createContext("someScope", ReportFeature.DELETE_SHARED_REPORT);
		reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertFalse(reportDTO.isDeletable());
		
		report = EntityUtil.createRandomReport();
		report.setCreatedBy("hugo");
		report.setIsShared(true);
		context = MockingUtils.createContext("someScope");
		reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertFalse(reportDTO.isDeletable());
		
		report = EntityUtil.createRandomReport();
		report.setCreatedBy("hugo");
		report.setIsShared(true);
		context = MockingUtils.createContext("someScope", ReportFeature.DELETE_REPORT);
		reportDTO = mapper.reportToReportDto(report, "georgi",  context);
		assertNotNull(report);
		assertFalse(reportDTO.isDeletable());
	}

}

package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.model.schemas.ReportGetStartAndEndDateRS;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.AreaFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.AssetFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.util.DTOUtil;
import lombok.SneakyThrows;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@Transactional
public class ReportServiceBeanIT {

    @EJB
    private ReportServiceBean reportBean;

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class,"reporting-resource.war").addPackages(true, "eu.europa.ec.fisheries.uvms.reporting.resource")
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsManifestResource(new File("src/test/resources/META-INF/jboss-deployment-structure.xml"))
                .addAsResource("config.properties")
                .addAsResource("usmDeploymentDescriptor.xml")
                .addAsResource("payloads/ReportDTOSerializerDeserializerTest.testWithoutFiltersWithoutDescription.json")
                .addAsResource("payloads/ReportDTOSerializerDeserializerTest.testWithFiltersWithCommonFilterWithSelectorAll.json")
                .addAsResource("payloads/ReportDTOSerializerDeserializerTest.testWithFiltersWithCommonFilterWithSelectorLastHours.json")
                .addAsResource("payloads/ReportDTOSerializerDeserializerTest.testWithFiltersWithCommonFilterWithSelectorLastPositions.json")
                .addAsResource("payloads/ReportDTOSerializerDeserializerTest.testWithFiltersWithCommonFilterWithSelectorLastPositionsWithStartDate.json")
                .addAsResource("payloads/ReportDTOSerializerDeserializerTest.testWithFiltersWithTracksNoFilterIds.json")
                .addAsResource("logback.xml")
                .addAsWebInfResource(new File("src/main/resources/META-INF/beans.xml"));

        File[] libs = Maven.resolver().loadPomFromFile("pom.xml").importDependencies(ScopeType.COMPILE, ScopeType.RUNTIME, ScopeType.TEST, ScopeType.PROVIDED).resolve().withTransitivity().asFile();
        war = war.addAsLibraries(libs);

        System.out.println(war.toString(true));

        return war;
    }

    @Before
    public void setUp() throws Exception {
        assertNotNull("Report not injected", reportBean);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void testGetReportDates() throws ReportingServiceException, IOException {

        URL url = Resources
                .getResource("payloads/ReportDTOSerializerDeserializerTest.testWithFiltersWithCommonFilterWithSelectorAll.json");
        String expected = Resources.toString(url, Charsets.UTF_8);
        ReportDTO deserialized = new ObjectMapper().readValue(expected, ReportDTO.class);
        deserialized.setId(null);
        deserialized.setScopeName("EC");
        deserialized.setCreatedBy("rep_power");

        ReportDTO savedReport = reportBean.create(deserialized);
        assertNotNull(savedReport);

        ReportGetStartAndEndDateRS response = reportBean.getReportDates("2016-03-18T11:00:00", savedReport.getId(), "rep_power", "EC");
        assertNotNull(response.getStartDate());
        assertNotNull(response.getEndDate());
    }


    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void testInterceptor() throws ReportingServiceException, ServiceException {
        ReportDTO report = reportBean.create(DTOUtil.createRandomReport());
        assertNotNull(report);

        reportBean.delete(report.getId(), report.getCreatedBy(), report.getScopeName(), false);
        report = reportBean.findById(report.getId(), report.getCreatedBy(), report.getScopeName(), false);
        assertNull(report);

    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void testCreateWithFilters() throws ReportingServiceException, ServiceException {
        ReportDTO newReport = DTOUtil.createRandomReport();

        AreaFilterDTO areaFilter = AreaFilterDTO.AreaFilterDTOBuilder().areaId(13L).areaType("eez").build();
        AssetFilterDTO assetFilterDTO = AssetFilterDTO.AssetFilterDTOBuilder().guid("213423423").name("asset2").build();
        List<FilterDTO> filters = new ArrayList<>();
        filters.add(areaFilter);
        filters.add(assetFilterDTO);

        newReport.setFilters(filters);

        ReportDTO report = reportBean.create(newReport);
        assertNotNull(report);

        assertEquals(report.getFilters().size(), filters.size());

        if (report.getFilters().get(0) instanceof AreaFilterDTO) {
            areaFilter = (AreaFilterDTO) report.getFilters().get(0);
            assetFilterDTO = (AssetFilterDTO) report.getFilters().get(1);
        } else {
            areaFilter = (AreaFilterDTO) report.getFilters().get(1);
            assetFilterDTO = (AssetFilterDTO) report.getFilters().get(0);
        }


        assertNotNull(areaFilter.getId());
        assertNotNull(assetFilterDTO.getId());

        reportBean.delete(report.getId(), report.getCreatedBy(), report.getScopeName(), false);
        report = reportBean.findById(report.getId(), report.getCreatedBy(), report.getScopeName(), false);
        assertNull(report);

    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    @SneakyThrows
    public void testCreateWithoutFiltersWithoutDescription(){

        URL url = Resources
                .getResource("payloads/ReportDTOSerializerDeserializerTest.testWithoutFiltersWithoutDescription.json");
        String expected = Resources.toString(url, Charsets.UTF_8);

        ReportDTO deserialized = new ObjectMapper().readValue(expected, ReportDTO.class);
        deserialized.setId(null);
        deserialized.setScopeName("test");
        deserialized.setCreatedBy("test");

        ReportDTO savedReport = reportBean.create(deserialized);

        assertTrue(savedReport.equals(deserialized));

    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    @SneakyThrows
    public void testCreateWithFiltersWithCommonFilterWithSelectorAll(){

        URL url = Resources
                .getResource("payloads/ReportDTOSerializerDeserializerTest.testWithFiltersWithCommonFilterWithSelectorAll.json");
        String expected = Resources.toString(url, Charsets.UTF_8);

        ReportDTO deserialized = new ObjectMapper().readValue(expected, ReportDTO.class);
        deserialized.setId(null);
        deserialized.setScopeName("test");
        deserialized.setCreatedBy("test");

        ReportDTO savedReport = reportBean.create(deserialized);

        assertTrue(savedReport.equals(deserialized));

    }

    @Test
     @Transactional(TransactionMode.ROLLBACK)
     @SneakyThrows
     public void testCreateWithFiltersWithCommonFilterWithSelectorLastHour(){

        URL url = Resources
                .getResource("payloads/ReportDTOSerializerDeserializerTest.testWithFiltersWithCommonFilterWithSelectorLastHours.json");
        String expected = Resources.toString(url, Charsets.UTF_8);

        ReportDTO deserialized = new ObjectMapper().readValue(expected, ReportDTO.class);
        deserialized.setId(null);
        deserialized.setScopeName("test");
        deserialized.setCreatedBy("test");

        ReportDTO savedReport = reportBean.create(deserialized);

        assertTrue(savedReport.equals(deserialized));

    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    @SneakyThrows
    public void testCreateWithFiltersWithCommonFilterWithSelectorLastPositions(){

        URL url = Resources
                .getResource("payloads/ReportDTOSerializerDeserializerTest.testWithFiltersWithCommonFilterWithSelectorLastPositions.json");
        String expected = Resources.toString(url, Charsets.UTF_8);

        ReportDTO deserialized = new ObjectMapper().readValue(expected, ReportDTO.class);
        deserialized.setId(null);
        deserialized.setScopeName("test");
        deserialized.setCreatedBy("test");

        ReportDTO savedReport = reportBean.create(deserialized);

        assertTrue(savedReport.equals(deserialized));

    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    @SneakyThrows
    public void testCreateWithFiltersWithCommonFilterWithSelectorLastPositionsWithStartDate(){

        URL url = Resources
                .getResource("payloads/ReportDTOSerializerDeserializerTest.testWithFiltersWithCommonFilterWithSelectorLastPositionsWithStartDate.json");
        String expected = Resources.toString(url, Charsets.UTF_8);

        ReportDTO deserialized = new ObjectMapper().readValue(expected, ReportDTO.class);
        deserialized.setId(null);
        deserialized.setScopeName("test");
        deserialized.setCreatedBy("test");

        ReportDTO savedReport = reportBean.create(deserialized);

        assertTrue(savedReport.equals(deserialized));

    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    @SneakyThrows
    public void testCreateWithFiltersWithTracks(){

        URL url = Resources
                .getResource("payloads/ReportDTOSerializerDeserializerTest.testWithFiltersWithTracksNoFilterIds.json");
        String expected = Resources.toString(url, Charsets.UTF_8);

        ReportDTO deserialized = new ObjectMapper().readValue(expected, ReportDTO.class);
        deserialized.setId(null);
        deserialized.setScopeName("test");
        deserialized.setCreatedBy("test");

        ReportDTO savedReport = reportBean.create(deserialized);

        assertTrue(savedReport.equals(deserialized));

    }

}

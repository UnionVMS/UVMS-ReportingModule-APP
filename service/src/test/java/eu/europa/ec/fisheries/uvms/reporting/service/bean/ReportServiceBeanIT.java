package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.AreaFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VesselFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.util.DTOUtil;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
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

import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;

@RunWith(Arquillian.class)
@Transactional
public class ReportServiceBeanIT {

    @EJB
    private ReportServiceBean reportBean;

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class,"reporting-service.war").addPackages(true, "eu.europa.ec.fisheries.uvms.reporting.service")
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsManifestResource(new File("src/test/resources/META-INF/jboss-deployment-structure.xml"))
                .addAsResource("config.properties")
                .addAsResource("usmDeploymentDescriptor.xml")
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
    public void testInterceptor() throws ReportingServiceException, ServiceException {
        ReportDTO report = reportBean.create(DTOUtil.createRandomReport());
        assertNotNull(report);

        reportBean.delete(report.getId(), report.getCreatedBy(), report.getScopeName());
        report = reportBean.findById(report.getId(), report.getCreatedBy(), report.getScopeName());
        assertNull(report);

    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void testCreateWithFilters() throws ReportingServiceException, ServiceException {
        ReportDTO newReport = DTOUtil.createRandomReport();

        AreaFilterDTO areaFilter = AreaFilterDTO.AreaFilterDTOBuilder().areaId(13L).areaType("eez").build();
        VesselFilterDTO vesselFilterDTO = VesselFilterDTO.VesselFilterDTOBuilder().guid("213423423").name("vessel2").build();
        List<FilterDTO> filters = new ArrayList<>();
        filters.add(areaFilter);
        filters.add(vesselFilterDTO);

        newReport.setFilters(filters);

        ReportDTO report = reportBean.create(newReport);
        assertNotNull(report);

        assertEquals(report.getFilters().size(), filters.size());

        if (report.getFilters().get(0) instanceof AreaFilterDTO) {
            areaFilter = (AreaFilterDTO) report.getFilters().get(0);
            vesselFilterDTO = (VesselFilterDTO) report.getFilters().get(1);
        } else {
            areaFilter = (AreaFilterDTO) report.getFilters().get(1);
            vesselFilterDTO = (VesselFilterDTO) report.getFilters().get(0);
        }


        assertNotNull(areaFilter.getId());
        assertNotNull(vesselFilterDTO.getId());

        reportBean.delete(report.getId(), report.getCreatedBy(), report.getScopeName());
        report = reportBean.findById(report.getId(), report.getCreatedBy(), report.getScopeName());
        assertNull(report);

    }
}

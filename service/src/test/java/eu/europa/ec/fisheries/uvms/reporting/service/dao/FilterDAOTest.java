package eu.europa.ec.fisheries.uvms.reporting.service.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.DateTimeFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Position;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.PositionSelector;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Selector;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselGroupFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsPositionFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.util.DTOUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.reflectionassert.ReflectionAssert;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static eu.europa.ec.fisheries.uvms.reporting.service.entities.DateTimeFilter.*;
import static eu.europa.ec.fisheries.uvms.reporting.service.entities.Report.*;
import static org.unitils.reflectionassert.ReflectionComparatorMode.*;

@RunWith(Arquillian.class)
@Transactional
public class FilterDAOTest {

    @PersistenceContext
    private EntityManager em;

    private FilterDAO dao;
    private ReportDAO reportDAO;

    @Before
    public void before(){
        dao = new FilterDAO(em);
        reportDAO = new ReportDAO(em);
    }

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "reporting-service.war")
                .addPackages(true, "eu.europa.ec.fisheries.uvms.reporting.service")
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsManifestResource(new File("src/test/resources/META-INF/jboss-deployment-structure.xml"))
                .addAsResource("config.properties")
                .addAsResource("logback.xml")
                .addAsWebInfResource(new File("src/main/resources/META-INF/beans.xml"));

        File[] libs = Maven.resolver().loadPomFromFile("pom.xml")
                .importDependencies(ScopeType.COMPILE, ScopeType.RUNTIME, ScopeType.TEST, ScopeType.PROVIDED)
                .resolve().withTransitivity().asFile();
        war = war.addAsLibraries(libs);

        System.out.println(war.toString(true));

        return war;
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void testListByReportId() throws ServiceException {

        Set<Filter> expectedCollection = new HashSet<>();

        Report report = DTOUtil.createRandomReportEntity();
        String user = "georgiTestttt12";
        report.setCreatedBy(user);
        report.setScopeName("356456731");
        report.setVisibility(VisibilityEnum.PRIVATE);
        Report savedReport = reportDAO.createEntity(report);

        VesselFilter filter1 = new VesselFilter();
        filter1.setReport(savedReport);
        filter1.setGuid("guid1");
        filter1.setName("vessel1");
        expectedCollection.add(filter1);
        dao.createEntity(filter1);

        VesselFilter filter2 = new VesselFilter();
        filter2.setReport(savedReport);
        filter2.setGuid("guid2");
        filter2.setName("vessel2");
        expectedCollection.add(filter2);
        dao.createEntity(filter2);

        VmsPositionFilter filter3 = new VmsPositionFilter();
        filter3.setMaximumSpeed("123");
        filter3.setMinimumSpeed("54654");
        filter3.setReport(savedReport);
        expectedCollection.add(filter3);
        dao.createEntity(filter3);

        VesselGroupFilter filter4 = new VesselGroupFilter();
        filter4.setReport(savedReport);
        filter4.setGroupId("1");
        filter4.setUserName("ffsdfsdfds");
        expectedCollection.add(filter4);
        dao.createEntity(filter4);

        VesselGroupFilter filter5 = new VesselGroupFilter();
        filter5.setReport(savedReport);
        filter5.setGroupId("2");
        filter5.setUserName("ffsdfsdfds");
        expectedCollection.add(filter5);
        dao.createEntity(filter5);

        DateTimeFilter filter6 = DateTimeFilterBuilder().build();
        filter6.setReport(savedReport);
        PositionSelector positionSelector = new PositionSelector();
        positionSelector.setPosition(Position.HOURS);
        positionSelector.setSelector(Selector.LAST);
        positionSelector.setValue(24L);
        filter6.setPositionSelector(positionSelector);
        expectedCollection.add(filter6);
        dao.createEntity(filter6);

        List<Filter> filters = dao.listByReportId(savedReport.getId());
        ReflectionAssert.assertReflectionEquals(expectedCollection, filters, LENIENT_ORDER);

    }

}

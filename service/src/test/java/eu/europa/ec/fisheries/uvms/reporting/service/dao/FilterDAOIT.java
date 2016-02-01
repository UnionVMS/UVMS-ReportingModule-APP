package eu.europa.ec.fisheries.uvms.reporting.service.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.*;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.CommonFilter;
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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.reflectionassert.ReflectionAssert;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.Assert.assertNull;
import static org.unitils.reflectionassert.ReflectionComparatorMode.*;

@RunWith(Arquillian.class)
@Transactional
public class FilterDAOIT {

    @PersistenceContext
    private EntityManager em;

    private FilterDAO filterDAO;
    private ReportDAO reportDAO;

    @Before
    public void before(){
        filterDAO = new FilterDAO(em);
        reportDAO = new ReportDAO(em);
    }

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "reporting-service.war")
                .addPackages(true, "eu.europa.ec.fisheries.uvms.reporting.service")
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsManifestResource(new File("src/test/resources/META-INF/jboss-deployment-structure.xml"))
                .addAsResource("usmDeploymentDescriptor.xml")
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
        report.getDetails().setCreatedBy(user);
        report.getDetails().setScopeName("356456731");
        report.setVisibility(VisibilityEnum.PRIVATE);
        Report savedReport = reportDAO.createEntity(report);

        AssetFilter filter1 = AssetFilter.builder().build();
        filter1.setReport(savedReport);
        filter1.setGuid("guid1");
        filter1.setName("asset1");
        expectedCollection.add(filter1);
        filterDAO.createEntity(filter1);

        AssetFilter filter2 = AssetFilter.builder().build();
        filter2.setReport(savedReport);
        filter2.setGuid("guid2");
        filter2.setName("asset2");
        expectedCollection.add(filter2);
        filterDAO.createEntity(filter2);

        VmsPositionFilter filter3 = VmsPositionFilter.builder().build();
        filter3.setMaximumSpeed(123F);
        filter3.setMinimumSpeed(54654F);
        filter3.setReport(savedReport);
        expectedCollection.add(filter3);
        filterDAO.createEntity(filter3);

        AssetGroupFilter filter4 = new AssetGroupFilter();
        filter4.setReport(savedReport);
        filter4.setGuid("1");
        filter4.setUserName("ffsdfsdfds");
        filter4.setName("name");
        expectedCollection.add(filter4);
        filterDAO.createEntity(filter4);

        AssetGroupFilter filter5 = new AssetGroupFilter();
        filter5.setReport(savedReport);
        filter5.setGuid("2");
        filter5.setUserName("ffsdfsdfds");
        filter5.setName("name2");
        expectedCollection.add(filter5);
        filterDAO.createEntity(filter5);

        CommonFilter filter6 = CommonFilter.builder().build();
        filter6.setReport(savedReport);
        filter6.setPositionSelector(
                PositionSelector.builder().selector(Selector.last).position(Position.hours).value(24F).build()
        );
        filter6.setReport(savedReport);
        expectedCollection.add(filter6);
        filterDAO.createEntity(filter6);

        VmsTrackFilter filter7 = VmsTrackFilter.builder()
                .timeRange(new TimeRange(13F, 10F))
                .durationRange(new DurationRange(11F, 12F))
                .build();
        filter7.setReport(savedReport);
        expectedCollection.add(filter7);
        filterDAO.createEntity(filter7);

        VmsSegmentFilter segmentFilter = VmsSegmentFilter.builder()
                .durationRange(new DurationRange(156F, 12F))
                .maximumSpeed(164F)
                .minimumSpeed(4565F)
                .build();
        segmentFilter.setReport(savedReport);
        segmentFilter.setReport(savedReport);
        expectedCollection.add(segmentFilter);
        filterDAO.createEntity(segmentFilter);

        CommonFilter commonFilter2 = CommonFilter.builder().build();
        commonFilter2.setReport(savedReport);
        commonFilter2.getDateRange().setEndDate(new Date());
        commonFilter2.setPositionSelector(
                PositionSelector.builder().selector(Selector.last).
                        position(Position.hours).value(24F).build()
        );
        commonFilter2.setReport(savedReport);
        expectedCollection.add(commonFilter2);
        filterDAO.createEntity(commonFilter2);

        AreaFilter filter8 = AreaFilter.builder().areaId(123456L).areaType("eez").build();
        filter8.setReport(savedReport);
        expectedCollection.add(filter8);
        filterDAO.createEntity(filter8);

        List<Filter> filters = filterDAO.listByReportId(savedReport.getId());
        ReflectionAssert.assertReflectionEquals(expectedCollection, filters, LENIENT_ORDER);

    }

    @Test
    @SneakyThrows
    public void testDeleteBy(){

        Report report = DTOUtil.createRandomReportEntity();
        String user = "georgiTestttt12";
        report.getDetails().setCreatedBy(user);
        report.getDetails().setScopeName("356456731");
        report.setVisibility(VisibilityEnum.PRIVATE);
        Report savedReport = reportDAO.createEntity(report);

        AreaFilter filter8 = AreaFilter.builder().areaId(123456L).areaType("eez").build();
        filter8.setReport(savedReport);
        Filter entity = filterDAO.createEntity(filter8);

        filterDAO.deleteBy(entity.getId());

        Report entityById = reportDAO.findEntityById(Report.class, savedReport.getId());

        assertNull(entityById.getFilters());
    }
}

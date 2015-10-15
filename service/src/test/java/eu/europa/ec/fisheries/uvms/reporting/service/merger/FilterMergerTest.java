package eu.europa.ec.fisheries.uvms.reporting.service.merger;

import eu.europa.ec.fisheries.schema.movement.v1.MovementActivityTypeType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.FilterDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.ReportDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.CommonFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VesselFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VesselGroupFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsPositionFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Selector;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselFilter;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.reporting.service.dto.CommonFilterDTO.CommonFilterDTOBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.PositionSelectorDTO.PositionSelectorDTOBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.VesselFilterDTO.VesselFilterDTOBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.VesselGroupFilterDTO.VesselGroupFilterDTOBuilder;
import static junit.framework.TestCase.assertTrue;

@Ignore
public class FilterMergerTest extends UnitilsJUnit4 {

    @PersistenceContext
    private EntityManager em;

    @TestedObject
    private FilterMerger merger = new FilterMerger(em);

    @InjectIntoByType
    private Mock<ReportDAO> reportDAOMock;

    @InjectIntoByType
    private Mock<FilterDAO> filterDAOMock;

    private VesselFilterDTO vessel1;
    private VesselGroupFilterDTO vgroup2;
    private VesselFilterDTO vessel2;
    private VesselGroupFilterDTO vgroup1;
    private VmsPositionFilterDTO vms;
    private CommonFilterDTO common;

    @Before
    public void before(){

        vessel1 = VesselFilterDTOBuilder().id(47L).guid("guid1").name("vessel1").build();

        vgroup2 = VesselGroupFilterDTOBuilder().id(48L).guid("2").userName("ddd").build();

        vessel2 = VesselFilterDTOBuilder().id(49L).guid("gui4564").name("vessel2").build();

        vgroup1 = VesselGroupFilterDTOBuilder().id(45L).guid("1").userName("ffsdfs").build();

        vms = VmsPositionFilterDTO.VmsPositionFilterDTOBuilder().id(1L)
                .movementActivity(MovementActivityTypeType.ANC)
                .minimumSpeed(100F)
                .maximumSpeed(123F)
                .movementType(MovementTypeType.ENT)
                .build();

        common = CommonFilterDTOBuilder()
                .id(46L)
                .positionSelector(PositionSelectorDTOBuilder().selector(Selector.all).build())
                .endDate(DateUtils.stringToDate("2015-10-09 08:56:48 +0200"))
                .startDate(DateUtils.stringToDate("2015-10-09 08:56:48 +0200"))
                .build();
    }


    @Test
    @SneakyThrows
    public void testMergeVesselFilterUntouched(){

        Collection<FilterDTO> collection =  new ArrayList<>();
        collection.add(vessel1);

        List<Filter> existingFilters = new ArrayList<>();
        VesselFilter existingFilter = VesselFilter.VesselFilterBuilder().id(47L).guid("guid1").name("vessel1").build();
        existingFilters.add(existingFilter);

        filterDAOMock.returns(existingFilters).listById(null);

        boolean updated = merger.merge(collection);

        filterDAOMock.assertNotInvoked().updateEntity(existingFilter);
        filterDAOMock.assertNotInvoked().deleteEntity(existingFilter, 47L);
        filterDAOMock.assertNotInvoked().createEntity(existingFilter);
        reportDAOMock.assertNotInvoked().updateEntity(null);
        reportDAOMock.assertNotInvoked().deleteEntity(null, 47L);
        reportDAOMock.assertNotInvoked().createEntity(null);
        assertTrue(!updated);

    }


    @Test
    @SneakyThrows
    public void testMergeVesselFilter(){

        Collection<FilterDTO> collection =  new ArrayList<>();
        collection.add(vessel1);

        List<Filter> existingFilters = new ArrayList<>();
        VesselFilter existingFilter = VesselFilter.VesselFilterBuilder().id(47L).guid("guid").name("vessel1").build();
        existingFilters.add(existingFilter);

        filterDAOMock.returns(existingFilters).listById(null);

        boolean updated = merger.merge(collection);

        filterDAOMock.assertInvoked().updateEntity(existingFilter);
        filterDAOMock.assertNotInvoked().deleteEntity(existingFilter, 47L);
        filterDAOMock.assertNotInvoked().createEntity(existingFilter);
        reportDAOMock.assertNotInvoked().updateEntity(null);
        reportDAOMock.assertNotInvoked().deleteEntity(null, 47L);
        reportDAOMock.assertNotInvoked().createEntity(null);
        assertTrue(updated);

    }

    @Test
    @SneakyThrows
    public void testMergeVesselFilterUpdateAndDelete(){

        Collection<FilterDTO> collection =  new ArrayList<>();
        collection.add(vessel1);

        List<Filter> existingFilters = new ArrayList<>();
        VesselFilter existingFilter = VesselFilter.VesselFilterBuilder().id(47L).guid("guid").name("vessel1").build();
        VesselFilter existingFilter2 = VesselFilter.VesselFilterBuilder().id(49L).guid("dddd").name("vvvv").build();

        existingFilters.add(existingFilter); // this one is an update
        existingFilters.add(existingFilter2); // this one has to go

        filterDAOMock.returns(existingFilters).listById(null);

        boolean updated = merger.merge(collection);

        filterDAOMock.assertInvoked().updateEntity(existingFilter);
        filterDAOMock.assertInvoked().deleteEntity(existingFilter2, 49L);
        filterDAOMock.assertNotInvoked().createEntity(existingFilter);
        reportDAOMock.assertNotInvoked().updateEntity(null);
        reportDAOMock.assertNotInvoked().deleteEntity(null, 47L);
        reportDAOMock.assertNotInvoked().createEntity(null);
        assertTrue(updated);
    }

    @Test
    @SneakyThrows
    public void testMergeVesselFilterInsert(){

        Collection<FilterDTO> collection =  new ArrayList<>();
        collection.add(vessel1);

        Report report = Report.ReportBuilder().id(47L).build();
        filterDAOMock.returns(new ArrayList<>()).listById(null); // empty array
        reportDAOMock.returns(report).findEntityById(Report.class, null);
        boolean updated = merger.merge(collection);

        filterDAOMock.assertNotInvoked().updateEntity(null);
        filterDAOMock.assertNotInvoked().deleteEntity(null, null);
        VesselFilter vesselFilter = VesselFilter.VesselFilterBuilder().build();
        filterDAOMock.assertInvoked().createEntity(vesselFilter);
        reportDAOMock.assertNotInvoked().updateEntity(null);
        reportDAOMock.assertNotInvoked().deleteEntity(null, 47L);
        reportDAOMock.assertNotInvoked().createEntity(null);
        assertTrue(updated);
    }

}

package eu.europa.ec.fisheries.uvms.reporting.service.merger;

import eu.europa.ec.fisheries.schema.movement.v1.MovementActivityTypeType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.FilterDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.ReportDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.*;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Selector;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselFilter;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

import static eu.europa.ec.fisheries.uvms.reporting.service.dto.CommonFilterDTO.CommonFilterDTOBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.PositionSelectorDTO.PositionSelectorDTOBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.VesselFilterDTO.VesselFilterDTOBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.VesselGroupFilterDTO.VesselGroupFilterDTOBuilder;
import static junit.framework.TestCase.assertTrue;

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

        vgroup2 = VesselGroupFilterDTOBuilder().id(48L).groupId("2").userName("ddd").build();

        vessel2 = VesselFilterDTOBuilder().id(49L).guid("gui4564").name("vessel2").build();

        vgroup1 = VesselGroupFilterDTOBuilder().id(45L).groupId("1").userName("ffsdfs").build();

        vms = VmsPositionFilterDTO.VmsPositionFilterDTOBuilder().id(1L)
                .movementActivity(MovementActivityTypeType.ANC)
                .minimumSpeed("100")
                .maximumSpeed("123")
                .movementType(MovementTypeType.ENT)
                .build();

        common = CommonFilterDTOBuilder()
                .id(46L)
                .positionSelector(PositionSelectorDTOBuilder().selector(Selector.ALL).build())
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
}

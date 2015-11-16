package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.SearchKey;
import eu.europa.ec.fisheries.uvms.exception.ProcessorException;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselGroupFilter;
import eu.europa.ec.fisheries.wsdl.vessel.group.VesselGroup;
import eu.europa.ec.fisheries.wsdl.vessel.types.ConfigSearchField;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListCriteriaPair;
import lombok.SneakyThrows;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;

public class FilterProcessorTest extends UnitilsJUnit4 {

    @TestedObject
    private FilterProcessor processor;

    @Test
    @SneakyThrows
    public void testInitWithVesselFilter() {

        Set<Filter> filterList = new HashSet<>();

        VesselFilter vesselFilter = VesselFilter.builder().guid("guid").build();
        filterList.add(vesselFilter);

        processor = new FilterProcessor(filterList);

        assertEquals(1, processor.getVesselListCriteriaPairs().size());
        assertEquals(0, processor.getRangeCriteria().size());
        assertEquals(0, processor.getVesselGroupList().size());
        assertEquals(1, processor.getMovementListCriteria().size());

        List<VesselListCriteriaPair> vesselListCriteriaPairList = new ArrayList<>();
        vesselListCriteriaPairList.addAll(processor.getVesselListCriteriaPairs());

        List<ListCriteria> listCriteria = new ArrayList<>();
        listCriteria.addAll(processor.getMovementListCriteria());

        processor.getVesselListCriteriaPairs();
        assertEquals(vesselListCriteriaPairList.get(0).getKey(), ConfigSearchField.GUID);
        assertEquals(vesselListCriteriaPairList.get(0).getValue(), "guid");

        assertEquals(listCriteria.get(0).getKey(), SearchKey.CONNECT_ID);
        assertEquals(listCriteria.get(0).getValue(), "guid");

    }

    @Test
    @SneakyThrows
    public void testInitWithVesselGroupFilter() {

        Set<Filter> filterList = new HashSet<>();

        VesselGroupFilter vesselGroupFilter = new VesselGroupFilter();
        vesselGroupFilter.setGuid("1");
        vesselGroupFilter.setUserName("test");

        filterList.add(vesselGroupFilter);

        processor = new FilterProcessor(filterList);

        List<VesselGroup> vesselGroupList = new ArrayList<>();
        vesselGroupList.addAll(processor.getVesselGroupList());

        assertEquals(0, processor.getVesselListCriteriaPairs().size());
        assertEquals(0, processor.getRangeCriteria().size());
        assertEquals(1, processor.getVesselGroupList().size());
        assertEquals(0, processor.getMovementListCriteria().size());

        assertEquals(vesselGroupList.get(0).getGuid(), "1");
        assertEquals(vesselGroupList.get(0).getName(), null);
        assertEquals(vesselGroupList.get(0).getUser(), "test");

    }

    @Test(expected = ProcessorException.class)
    @SneakyThrows
    public void shouldThrowExceptionWhenEmptyFilterList() {
        Set<Filter> filterList = new HashSet<>();
        processor = new FilterProcessor(filterList);
    }

    @Test(expected = ProcessorException.class)
    @SneakyThrows
    public void testSanityFilterSetNull() {
        processor = new FilterProcessor(null);
    }

}

package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.schema.movement.search.v1.SearchKey;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.exception.ProcessorException;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.*;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.CommonFilter;
import eu.europa.ec.fisheries.wsdl.vessel.types.ConfigSearchField;
import lombok.SneakyThrows;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static eu.europa.ec.fisheries.uvms.reporting.service.entities.CommonFilter.CommonFilterBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.entities.PositionSelector.PositionSelectorBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselFilter.VesselFilterBuilder;
import static junit.framework.TestCase.assertEquals;

public class FilterProcessorTest extends UnitilsJUnit4 {

    @TestedObject
    private FilterProcessor processor;

    @Test
    @SneakyThrows
    public void testInitWithVesselFilter() {

        Set<Filter> filterList = new HashSet<>();

        VesselFilter vesselFilter = VesselFilterBuilder().guid("guid").build();
        filterList.add(vesselFilter);

        processor = new FilterProcessor(filterList);

        assertEquals(1, processor.getVesselListCriteriaPairs().size());
        assertEquals(0, processor.getRangeCriteria().size());
        assertEquals(0, processor.getVesselGroupList().size());
        assertEquals(1, processor.getMovementListCriteria().size());

        assertEquals(processor.getVesselListCriteriaPairs().get(0).getKey(), ConfigSearchField.GUID);
        assertEquals(processor.getVesselListCriteriaPairs().get(0).getValue(), "guid");

        assertEquals(processor.getMovementListCriteria().get(0).getKey(), SearchKey.CONNECT_ID);
        assertEquals(processor.getMovementListCriteria().get(0).getValue(), "guid");

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

        assertEquals(0, processor.getVesselListCriteriaPairs().size());
        assertEquals(0, processor.getRangeCriteria().size());
        assertEquals(1, processor.getVesselGroupList().size());
        assertEquals(0, processor.getMovementListCriteria().size());

        assertEquals(processor.getVesselGroupList().get(0).getGuid(), "1");
        assertEquals(processor.getVesselGroupList().get(0).getName(), null);
        assertEquals(processor.getVesselGroupList().get(0).getUser(), "test");

    }

    @Test(expected = ProcessorException.class)
    @SneakyThrows
    public void testSanityFilterNull() {
        Set<Filter> filterList = new HashSet<>();
        processor = new FilterProcessor(filterList);
    }

    @Test(expected = ProcessorException.class)
    @SneakyThrows
    public void testSanityFilterSetNull() {
        processor = new FilterProcessor(null);
    }

}

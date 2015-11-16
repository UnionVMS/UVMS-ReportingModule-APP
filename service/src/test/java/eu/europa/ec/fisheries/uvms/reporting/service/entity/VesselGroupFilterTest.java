package eu.europa.ec.fisheries.uvms.reporting.service.entity;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselGroupFilter;
import eu.europa.ec.fisheries.wsdl.vessel.group.VesselGroup;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class VesselGroupFilterTest extends UnitilsJUnit4 {

    @TestedObject
    private VesselGroupFilter filter;

    @Before
    public void before(){
        filter = new VesselGroupFilter();
    }

    @Test
    public void testVesselGroupCriteria(){

        filter = VesselGroupFilter.builder().groupId("5").name("GP5").userName("test").build();

        List<VesselGroup> vesselGroups = filter.vesselGroupCriteria();

        assertEquals(1, vesselGroups.size());
        assertEquals(vesselGroups.get(0).getName(), filter.getName());
        assertEquals(vesselGroups.get(0).getUser(), filter.getUserName());
        assertEquals(vesselGroups.get(0).getGuid(), filter.getGuid());
    }

    @Test
    public void testMerge(){

       filter = VesselGroupFilter.builder().groupId("2").name("GP2").build();

        VesselGroupFilter incoming = VesselGroupFilter.builder().groupId("1").name("GP1").build();

        assertNotEquals(filter, incoming);

        filter.merge(incoming);

        assertEquals(filter, incoming);

    }

}

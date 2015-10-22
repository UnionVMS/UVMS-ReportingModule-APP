package eu.europa.ec.fisheries.uvms.reporting.service.entity;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsTrackFilter;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

import static eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsTrackFilter.TrackFilterBuilder;
import static org.junit.Assert.assertEquals;

public class VmsTrackFilterTest extends UnitilsJUnit4 {

    @TestedObject
    private VmsTrackFilter filter = TrackFilterBuilder().build();

    @Test
    public void testMerge(){

        VmsTrackFilter incoming = TrackFilterBuilder()
                .minTime(10F)
                .maxTime(20F)
                .minDuration(200F)
                .maxDuration(100F)
                .build();

        filter.merge(incoming);

        assertEquals(incoming, filter);
    }
}

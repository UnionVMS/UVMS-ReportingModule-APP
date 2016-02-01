package eu.europa.ec.fisheries.uvms.reporting.service.entity;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.DurationRange;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.TimeRange;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsTrackFilter;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

import static org.junit.Assert.assertEquals;

public class VmsTrackFilterTest extends UnitilsJUnit4 {

    @TestedObject
    private VmsTrackFilter filter = VmsTrackFilter.builder().build();

    @Test
    public void testMerge(){

        VmsTrackFilter incoming = VmsTrackFilter.builder()
                .timeRange(new TimeRange(10F, 20F))
                .durationRange(new DurationRange(200F, 100F))
                .build();

        filter.merge(incoming);

        assertEquals(incoming, filter);
    }
}

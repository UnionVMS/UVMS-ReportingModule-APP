package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.TrackFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.DurationRange;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.TimeRange;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsTrackFilter;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@Ignore
public class FilterToDTOVisitorTest {

    private Filter.FilterToDTOVisitor visitor = new Filter.FilterToDTOVisitor();

    @Test
    public void shouldReturnTrackFilterDTO(){

        VmsTrackFilter filter = VmsTrackFilter.builder()
                .durationRange(new DurationRange(5F, 500F))
                .minAvgSpeed(50F).maxAvgSpeed(100F)
                .timeRange(new TimeRange(10F, 20F))
                .reportId(1L)
                .id(1L)
                .build();

        TrackFilterDTO expected = TrackFilterDTO.builder()
                .minDistance(10F).maxDistance(100F)
                .minDuration(5F).maxDuration(500F)
                .minAvgSpeed(50F).maxAvgSpeed(100F)
                .minTime(10F).maxTime(20F)
                .id(1L)
                .build();

        TrackFilterDTO accept = (TrackFilterDTO) filter.accept(visitor);

        assertEquals(expected, accept);

    }

}

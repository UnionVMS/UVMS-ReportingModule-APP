package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.TrackFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.DistanceRange;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.DurationRange;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.TimeRange;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsTrackFilter;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertEquals;

@RunWith(JUnitParamsRunner.class)
public class VmsTrackFilterTest {

    @Test
    @Parameters(method = "filterValues")
    public void shouldConvertToDto(VmsTrackFilter filter, TrackFilterDTO expectedResult){

        TrackFilterDTO trackFilterDTO = TrackFilterMapper.INSTANCE.trackFilterToTrackFilterDTO(filter);
        assertEquals(expectedResult, trackFilterDTO);

    }

    @Test
    @Parameters(method = "filterValues")
    public void shouldConvertToEntity(VmsTrackFilter expectedResult, TrackFilterDTO dto){

        VmsTrackFilter trackFilter = TrackFilterMapper.INSTANCE.trackFilterDTOToTrackFilter(dto);
        assertEquals(expectedResult, trackFilter);

    }

    protected Object[] filterValues(){

        VmsTrackFilter trackFilter1 = VmsTrackFilter
                .builder()
                    .timeRange(new TimeRange(null, null))
                    .minAvgSpeed(null).maxAvgSpeed(null)
                    .durationRange(new DurationRange(null, null))
                    .distanceRange(new DistanceRange(null, null))
                .build();

        TrackFilterDTO expected1 = TrackFilterDTO.builder().build();

        VmsTrackFilter trackFilter2 = VmsTrackFilter
                .builder()
                .timeRange(new TimeRange(10F, null))
                .minAvgSpeed(null).maxAvgSpeed(null)
                .durationRange(new DurationRange(null, null))
                .distanceRange(new DistanceRange(null, null))
                .build();

        TrackFilterDTO expected2 = TrackFilterDTO.builder()
                .minTime(10F)
                .build();

        VmsTrackFilter trackFilter3 = VmsTrackFilter
                .builder()
                .timeRange(new TimeRange(10F, 100F))
                .minAvgSpeed(null).maxAvgSpeed(null)
                .durationRange(new DurationRange(null, null))
                .distanceRange(new DistanceRange(null, null))
                .build();

        TrackFilterDTO expected3 = TrackFilterDTO.builder()
                .minTime(10F).maxTime(100F)
                .build();

        return $(
                $(trackFilter1, expected1),
                $(trackFilter2, expected2),
                $(trackFilter3, expected3)


        );
    }

}

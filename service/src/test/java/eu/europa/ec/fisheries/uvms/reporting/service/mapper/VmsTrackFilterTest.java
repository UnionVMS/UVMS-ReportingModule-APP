/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.commons.domain.Range;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.TrackFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.DurationRange;
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

        TrackFilterDTO trackFilterDTO = VmsTrackFilterMapper.INSTANCE.trackFilterToTrackFilterDTO(filter);
        assertEquals(expectedResult, trackFilterDTO);

    }

    @Test
    @Parameters(method = "filterValues")
    public void shouldConvertToEntity(VmsTrackFilter expectedResult, TrackFilterDTO dto){

        VmsTrackFilter trackFilter = VmsTrackFilterMapper.INSTANCE.trackFilterDTOToTrackFilter(dto);
        assertEquals(expectedResult.toString(), trackFilter.toString());

    }

    protected Object[] filterValues(){

        VmsTrackFilter trackFilter1 = VmsTrackFilter
                .builder()
                    .timeRange(new Range(null, null))
                    .minAvgSpeed(null).maxAvgSpeed(null)
                    .durationRange(new DurationRange(null, null))
                .build();

        TrackFilterDTO expected1 = TrackFilterDTO.builder().build();

        VmsTrackFilter trackFilter2 = VmsTrackFilter
                .builder()
                .timeRange(new Range(10F, null))
                .minAvgSpeed(null).maxAvgSpeed(null)
                .durationRange(new DurationRange(null, null))
                .build();

        TrackFilterDTO expected2 = TrackFilterDTO.builder()
                .minTime(10F)
                .build();

        VmsTrackFilter trackFilter3 = VmsTrackFilter
                .builder()
                .timeRange(new Range(10F, 100F))
                .minAvgSpeed(null).maxAvgSpeed(null)
                .durationRange(new DurationRange(null, null))
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
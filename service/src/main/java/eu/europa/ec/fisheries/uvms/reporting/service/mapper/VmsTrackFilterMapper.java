/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.schema.movement.search.v1.RangeCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeKeyType;
import eu.europa.ec.fisheries.uvms.domain.Range;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsTrack;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.TrackFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.entities.DistanceRange;
import eu.europa.ec.fisheries.uvms.reporting.entities.DurationRange;
import eu.europa.ec.fisheries.uvms.reporting.entities.VmsTrackFilter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(imports = {DurationRange.class, DistanceRange.class, Range.class, RangeKeyType.class})
public interface VmsTrackFilterMapper {

   VmsTrackFilterMapper INSTANCE = Mappers.getMapper(VmsTrackFilterMapper.class);

    @Mappings({
            @Mapping(source = "durationRange.minDuration", target = "minDuration"),
            @Mapping(source = "durationRange.maxDuration", target = "maxDuration"),
            @Mapping(source = "timeRange.min", target = "minTime"),
            @Mapping(source = "timeRange.max", target = "maxTime")
    })
    TrackFilterDTO trackFilterToTrackFilterDTO(VmsTrackFilter vmsTrackFilter);

    @Mappings({
            @Mapping(target = "durationRange", expression = "java(new DurationRange(dto.getMinDuration(), dto.getMaxDuration()))"),
            @Mapping(target = "timeRange", expression = "java(new Range(dto.getMinTime(), dto.getMaxTime()))")
    })
    VmsTrackFilter trackFilterDTOToTrackFilter(TrackFilterDTO dto); // TODO refactor with Tracks

    @Mappings({
            @Mapping(constant = "TRACK_SPEED", target = "key"),
            @Mapping(source = "minAvgSpeed", target = "from", defaultValue = "0"),
            @Mapping(source = "maxAvgSpeed", target = "to", defaultValue = "9223372036854775807")
    })
    RangeCriteria speedRangeToRangeCriteria(VmsTrackFilter trackFilter);

    @Mappings({
            @Mapping(constant = "TRACK_DURATION", target = "key"),
            @Mapping(target = "from", expression = "java(trackFilter.convertedMinDuration())"),
            @Mapping(target = "to", expression = "java(trackFilter.convertedMaxDuration())")
    })
    RangeCriteria durationRangeToRangeCriteria(VmsTrackFilter trackFilter);

    @Mappings({
            @Mapping(constant = "TRACK_DURATION_AT_SEA", target = "key"),
            @Mapping(target = "from", expression = "java(trackFilter.convertedMinTime())"),
            @Mapping(target = "to", expression = "java(trackFilter.convertedMaxTime())")
    })
    RangeCriteria timeRangeToRangeCriteria(VmsTrackFilter trackFilter);

    @Mappings({
            @Mapping(target = "durationRange", expression = "java(new DurationRange(dto.getTrkMinDuration(), dto.getTrkMaxDuration()))"),
            @Mapping(target = "timeRange", expression = "java(new Range(dto.getTrkMinTime(), dto.getTrkMaxTime()))")
    })
    VmsTrackFilter tracksToVmsTrackFilter(VmsTrack dto);

    @Mappings({
            @Mapping(target = "report", ignore = true),
            @Mapping(target = "reportId", ignore = true)
    })
    void merge(VmsTrackFilter incoming, @MappingTarget VmsTrackFilter current);
}
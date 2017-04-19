/*
 *
 * Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */
package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingActivitySummary;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.VesselIdentifierType;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FishingActivitySummaryDTO;
import org.apache.commons.collections.CollectionUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sanera on 19/04/2017.
 */
@Mapper
public abstract class FishingActivityMapper {

    public static final FishingActivityMapper INSTANCE = Mappers.getMapper(FishingActivityMapper.class);

    @Mappings({
           @Mapping(target = "vesselIdentifiers", expression = "java(convertVesselIdentifierTypeToMap(activitySummary))")
    })
    public abstract FishingActivitySummaryDTO activitySummaryToFishingActivitySummaryDTO(FishingActivitySummary activitySummary);

    protected Map<String,String> convertVesselIdentifierTypeToMap( FishingActivitySummary activitySummary){
        if(activitySummary==null || CollectionUtils.isEmpty(activitySummary.getVesselIdentifiers()))
            return null;

        Map<String,String> vesselIdMap = new HashMap<>();

        for(VesselIdentifierType vesselIdentifierType : activitySummary.getVesselIdentifiers()){
            vesselIdMap.put(vesselIdentifierType.getKey().toString(),vesselIdentifierType.getValue());
        }
        return vesselIdMap;
    }
}

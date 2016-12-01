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

import eu.europa.ec.fisheries.uvms.reporting.service.dto.FaFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FaWeightDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FaFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FaGear;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FaPort;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FaWeight;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * Created by padhyad on 11/16/2016.
 */
@Mapper
public interface FaFilterMapper {

    FaFilterMapper INSTANCE = Mappers.getMapper(FaFilterMapper.class);

    FaFilter faFilterDtoToFaFilter(FaFilterDTO faFilterDTO);

    FaFilterDTO faFilterToFaFilterDto(FaFilter faFilter);

    FaWeight faWeightDtoToFaWeight(FaWeightDTO faWeightDTO);

    FaWeightDTO faWeightToFaWeightDTO(FaWeight faWeight);

    @Mappings({
            @Mapping(target = "report", ignore = true),
            @Mapping(target = "reportId", ignore = true)
    })
    void merge(FaFilter filter, @MappingTarget FaFilter faFilter);
}

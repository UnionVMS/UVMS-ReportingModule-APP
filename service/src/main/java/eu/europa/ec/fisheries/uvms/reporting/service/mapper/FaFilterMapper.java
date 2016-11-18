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
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FaFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FaGear;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FaPort;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FaWeight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * Created by padhyad on 11/16/2016.
 */
@Mapper
public interface FaFilterMapper {

    FaFilterMapper INSTANCE = Mappers.getMapper(FaFilterMapper.class);

    @Mappings({
            @Mapping(target = "faPort", expression = "java(faFilterDtoToFaPort(faFilterDTO))"),
            @Mapping(target = "faGear", expression = "java(faFilterDtoToFaGear(faFilterDTO))"),
            @Mapping(target = "faWeight", expression = "java(faFilterDtoToFaWeight(faFilterDTO))")
    })
    FaFilter faFilterDtoToFaFilter(FaFilterDTO faFilterDTO);

    @Mappings({
            @Mapping(target = "departure", source = "departurePort"),
            @Mapping(target = "arrival", source = "arrivalPort"),
            @Mapping(target = "landing", source = "landingPort")
    })
    FaPort faFilterDtoToFaPort(FaFilterDTO faFilterDTO);

    @Mappings({
            @Mapping(target = "onboard", source = "gearOnboard"),
            @Mapping(target = "deployed", source = "gearDeployed")
    })
    FaGear faFilterDtoToFaGear(FaFilterDTO faFilterDTO);

    FaWeight faFilterDtoToFaWeight(FaFilterDTO faFilterDTO);

    @Mappings({
            @Mapping(target = "departurePort", source = "faPort.departure"),
            @Mapping(target = "arrivalPort", source = "faPort.arrival"),
            @Mapping(target = "landingPort", source = "faPort.landing"),
            @Mapping(target = "gearOnboard", source = "faGear.onboard"),
            @Mapping(target = "gearDeployed", source = "faGear.deployed"),
            @Mapping(target = "weightMin", source = "faWeight.weightMin"),
            @Mapping(target = "weightMax", source = "faWeight.weightMax"),
            @Mapping(target = "weightUnit", source = "faWeight.weightUnit")
    })
    FaFilterDTO FaFilterToFaFilterDTO(FaFilter faFilter);

    @Mappings({
            @Mapping(target = "report", ignore = true),
            @Mapping(target = "reportId", ignore = true)
    })
    void merge(FaFilter filter, @MappingTarget FaFilter faFilter);
}

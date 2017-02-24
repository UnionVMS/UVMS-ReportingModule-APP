/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.Asset;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.AssetGroupFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.AssetGroupFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.util.ObjectFactory;
import eu.europa.ec.fisheries.wsdl.asset.group.AssetGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ObjectFactory.class)
public interface AssetGroupFilterMapper {

    AssetGroupFilterMapper INSTANCE = Mappers.getMapper(AssetGroupFilterMapper.class);

    AssetGroupFilterDTO assetGroupFilterToAssetGroupFilterDTO(AssetGroupFilter assetGroupFilter);

    AssetGroupFilter assetGroupFilterDTOToAssetGroupFilter(AssetGroupFilterDTO assetGroupFilterDTO);

    @Mappings({
            @Mapping(constant = "true", target = "dynamic"),
            @Mapping(source = "userName", target = "user"),
    })
    AssetGroup assetGroupFilterToAssetGroup(AssetGroupFilter assetGroupFilter);

    @Mappings({
            @Mapping(source = "user", target = "userName"),
    })
    AssetGroupFilter assetToAssetFilterGroup(Asset dto);

    @Mappings({
            @Mapping(target = "report", ignore = true),
            @Mapping(target = "reportId", ignore = true)
    })
    void merge(AssetGroupFilter incoming, @MappingTarget AssetGroupFilter current);
}
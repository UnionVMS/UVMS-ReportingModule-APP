/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import java.util.List;
import java.util.Set;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.Asset;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.AssetFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.AssetFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.util.ObjectFactory;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListCriteriaPair;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ObjectFactory.class)
public interface AssetFilterMapper {

    AssetFilterMapper INSTANCE = Mappers.getMapper(AssetFilterMapper.class);

    AssetFilterDTO assetFilterToAssetFilterDTO(AssetFilter assetFilter);

    AssetFilter assetFilterDTOToAssetFilter(AssetFilterDTO assetFilterDTO);

    AssetFilter assetToAssetFilter(Asset dto);

    Set<AssetFilter> assetListToAssetFilterSet(List<Asset> list);

    @Mappings({
            @Mapping(source = "guid", target = "value"),
            @Mapping(constant = "HIST_GUID", target = "key"),
    })
    AssetListCriteriaPair assetFilterToAssetListCriteriaPair(AssetFilter assetFilter);

    Set<AssetListCriteriaPair> assetFilterListToAssetListCriteriaPairList(Set<AssetFilter> assetFilter);

    @Mappings({
            @Mapping(source = "guid", target = "value"),
            @Mapping(constant = "CONNECT_ID", target = "key"),
    })
    ListCriteria assetFilterToListCriteria(AssetFilter assetFilter);

    @Mappings({
            @Mapping(target = "report", ignore = true),
            @Mapping(target = "reportId", ignore = true)
    })
    void merge(AssetFilter incoming, @MappingTarget AssetFilter current);

}
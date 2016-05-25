package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.model.vms.Asset;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.AssetGroupFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.AssetFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.AssetGroupFilter;
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

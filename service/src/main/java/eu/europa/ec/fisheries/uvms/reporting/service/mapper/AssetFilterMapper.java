package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.uvms.reporting.model.vms.Asset;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.AssetFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.AssetFilter;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListCriteriaPair;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import java.util.List;
import java.util.Set;

@Mapper(uses = ObjectFactory.class)
public interface AssetFilterMapper {

    AssetFilterMapper INSTANCE = Mappers.getMapper(AssetFilterMapper.class);

    AssetFilterDTO assetFilterToAssetFilterDTO(AssetFilter assetFilter);

    AssetFilter assetFilterDTOToAssetFilter(AssetFilterDTO assetFilterDTO);

    AssetFilter assetToAssetFilter(Asset dto);

    Set<AssetFilter> assetListToAssetFilterSet(List<Asset> list);

    @Mappings({
            @Mapping(source = "guid", target = "value"),
            @Mapping(constant = "GUID", target = "key"),
    })
    AssetListCriteriaPair assetFilterToAssetListCriteriaPair(AssetFilter assetFilter);

    @Mappings({
            @Mapping(source = "guid", target = "value"),
            @Mapping(constant = "CONNECT_ID", target = "key"),
    })
    ListCriteria assetFilterToListCriteria(AssetFilter assetFilter);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "report", ignore = true),
            @Mapping(target = "reportId", ignore = true)
    })
    void merge(AssetFilter incoming, @MappingTarget AssetFilter current);

}

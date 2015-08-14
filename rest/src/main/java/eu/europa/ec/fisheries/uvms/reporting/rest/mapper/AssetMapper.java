package eu.europa.ec.fisheries.uvms.reporting.rest.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.AssetDto;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * //TODO add test
 */
@Mapper
public interface AssetMapper {

    AssetMapper INSTANCE = Mappers.getMapper(AssetMapper.class);

    AssetDto vesselToAssetDto(Vessel vessel);
}

package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialModuleMethod;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialModuleRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.StringWrapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(imports = JAXBMarshaller.class, uses = eu.europa.ec.fisheries.uvms.spatial.model.schemas.ObjectFactory.class)
public interface SpatialModuleMapper {

    SpatialModuleMapper INSTANCE = Mappers.getMapper(SpatialModuleMapper.class);

    @Mappings({
            @Mapping(target = "mapConfiguration", source = "config")
    })
    SpatialSaveOrUpdateMapConfigurationRQ configToSaveOrUpdateRequest(MapConfigurationType config, SpatialModuleMethod method);

    @Mappings({
            @Mapping(target = "value", expression = "java(JAXBMarshaller.marshall(request))")
    })
    StringWrapper marshal(SpatialModuleRequest request) throws SpatialModelMarshallException;

}

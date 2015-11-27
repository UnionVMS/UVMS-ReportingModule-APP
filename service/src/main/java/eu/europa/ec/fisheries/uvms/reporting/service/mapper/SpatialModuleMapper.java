package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Marshaled;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialModuleRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRQ;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(imports = JAXBMarshaller.class, uses = ObjectFactory.class)
public abstract class SpatialModuleMapper {

    public static SpatialModuleMapper INSTANCE = Mappers.getMapper(SpatialModuleMapper.class);

    @Mappings({
        @Mapping(constant = "UPDATE_MAP_CONFIG", target = "method")
    })
    public abstract SpatialSaveOrUpdateMapConfigurationRQ mapToUpdateMapRequest(MapConfigurationType config);

    @Mappings({
            @Mapping(target = "value", expression = "java(JAXBMarshaller.marshall(request))")
    })
    public abstract Marshaled marshal(SpatialModuleRequest request) throws SpatialModelMarshallException;

}

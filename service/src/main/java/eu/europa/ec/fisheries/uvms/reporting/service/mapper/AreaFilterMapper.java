package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.AreaFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.AreaFilter;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ObjectFactory.class)
public interface AreaFilterMapper {

    AreaFilterMapper INSTANCE = Mappers.getMapper(AreaFilterMapper.class);

    AreaFilterDTO areaFilterToAreaFilterDTO(AreaFilter areaFilter);

    AreaFilter areaFilterDTOToAreaFilter(AreaFilterDTO areaFilterDTO);

    AreaIdentifierType areaIdentifierTypeToAreaFilter(AreaFilter areaFilter);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "report", ignore = true),
            @Mapping(target = "reportId", ignore = true)
    })
    void merge(AreaFilter incoming, @MappingTarget AreaFilter current);

}

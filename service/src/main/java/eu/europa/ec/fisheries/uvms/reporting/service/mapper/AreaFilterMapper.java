package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.model.vms.Area;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.AreaFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.AreaFilter;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import java.util.List;
import java.util.Set;

@Mapper(uses = ObjectFactory.class)
public interface AreaFilterMapper {

    AreaFilterMapper INSTANCE = Mappers.getMapper(AreaFilterMapper.class);

    AreaFilterDTO areaFilterToAreaFilterDTO(AreaFilter areaFilter); // TODO user Area from model

    AreaFilter areaFilterDTOToAreaFilter(AreaFilterDTO areaFilterDTO); // TODO user Area from model

    @Mappings({
            @Mapping(source="gid", target = "areaId")
    })
    AreaFilter areaToAreaFilter(Area area);

    Set<AreaFilter> arealistToAreaFilterSet(List<Area> areaList);

    @Mappings({
            @Mapping(source="areaId", target = "id")
    })
    AreaIdentifierType areaIdentifierTypeToAreaFilter(AreaFilter areaFilter);

    @Mappings({
            @Mapping(target = "report", ignore = true),
            @Mapping(target = "reportId", ignore = true)
    })
    void merge(AreaFilter incoming, @MappingTarget AreaFilter current);

}

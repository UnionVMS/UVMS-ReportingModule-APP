package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.CriteriaFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.CriteriaFilter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ObjectFactory.class})
public interface CriteriaFilterMapper {

    CriteriaFilterMapper INSTANCE = Mappers.getMapper(CriteriaFilterMapper.class);

    CriteriaFilter criteriaFilterDTOToCriteriaFilter(CriteriaFilterDTO dto);

    CriteriaFilterDTO criteriaFilterToCriteriaFilterDTO(CriteriaFilter entity);

    @Mappings({
            @Mapping(target = "report", ignore = true),
            @Mapping(target = "reportId", ignore = true),
    })
    void merge(CriteriaFilter incoming, @MappingTarget CriteriaFilter current);

}

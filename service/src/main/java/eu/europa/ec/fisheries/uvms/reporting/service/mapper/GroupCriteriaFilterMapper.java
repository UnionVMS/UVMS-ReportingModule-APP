package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import java.util.List;

import eu.europa.ec.fisheries.uvms.activity.model.schemas.GroupCriteria;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.CriteriaFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.GroupCriteriaFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.enums.GroupCriteriaType;
import eu.europa.ec.fisheries.uvms.reporting.service.util.ObjectFactory;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ValueMappings;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ObjectFactory.class})
public interface GroupCriteriaFilterMapper {

    GroupCriteriaFilterMapper INSTANCE = Mappers.getMapper(GroupCriteriaFilterMapper.class);

    GroupCriteriaFilter mapCriteriaFilterDTOToCriteriaFilter(CriteriaFilterDTO dto);

    @InheritInverseConfiguration
    CriteriaFilterDTO mapCriteriaFilterToCriteriaFilterDTO(GroupCriteriaFilter entity);

    @Mappings({
            @Mapping(target = "report", ignore = true),
            @Mapping(target = "reportId", ignore = true),
    })
    void merge(GroupCriteriaFilter incoming, @MappingTarget GroupCriteriaFilter current);

    @IterableMapping(elementTargetType = String.class)
    List<String> mapStringListToGroupCriteriaTypeList(List<GroupCriteriaType> value);

    @InheritInverseConfiguration
    List<GroupCriteriaType> mapGroupCriteriaTypeListToStringList(List<String> value);

    List<GroupCriteria> mapGroupCriteriaTypeListToGroupCriteriaList(List<GroupCriteriaType> value);

    @InheritInverseConfiguration
    List<GroupCriteriaType> mapGroupCriteriaListToGroupCriteriaTypeList(List<GroupCriteria> value);

    @ValueMappings({
            //@ValueMapping(source = "AREA",target = "<NULL>")
    })
    GroupCriteriaType mapGroupCriteriaToGroupCriteriaType(GroupCriteria value);

    @InheritInverseConfiguration
    GroupCriteria mapGroupCriteriaTypeToGroupCriteria(GroupCriteriaType value);

}

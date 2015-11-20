package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReportDetailsMapper {

    ReportDetailsMapper INSTANCE = Mappers.getMapper(ReportDetailsMapper.class);

    @Mappings({
            @Mapping(target = "scopeName", ignore = true),
            @Mapping(target = "createdBy", ignore = true)
    })
    void merge(ReportDetails incoming, @MappingTarget ReportDetails current);
}

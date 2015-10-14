package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.AreaFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.AreaFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.TrackFilter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created by georgige on 10/13/2015.
 */
@Mapper(uses = ObjectFactory.class)
public interface AreaFilterMapper {

    AreaFilterMapper INSTANCE = Mappers.getMapper(AreaFilterMapper.class);

    AreaFilterDTO areaFilterToAreaFilterDTO(AreaFilter areaFilter);

    AreaFilter areaFilterDTOToAreaFilter(AreaFilterDTO areaFilterDTO);

}

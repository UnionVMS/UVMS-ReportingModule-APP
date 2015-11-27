package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.MapConfigurationDTO;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class MapConfigMapper {

    public static MapConfigMapper INSTANCE = Mappers.getMapper(MapConfigMapper.class);

    public abstract MapConfigurationType configAndReportToMapConfigurationType(Long reportId, MapConfigurationDTO incoming);

    @BeforeMapping
    protected void validate(MapConfigurationDTO incoming) {

        if (incoming != null) {

            if (incoming.getCoordinatesFormat() == null && incoming.getDisplayProjectionId() == null

                    && incoming.getMapProjectionId() == null && incoming.getScaleBarUnits() == null)

            {

                throw new IllegalArgumentException("At least one map configuration attribute should be specified");

            }

        }

    }
}

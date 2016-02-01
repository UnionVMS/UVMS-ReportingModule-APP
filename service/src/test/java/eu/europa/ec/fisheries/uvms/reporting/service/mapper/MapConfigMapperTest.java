package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.MapConfigurationDTO;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ScaleBarUnits;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MapConfigMapperTest {

    private static final Long SPATIAL_CONNECT_ID = new Long(3);
    private static final Long DISPLAY_PROJECTION_ID = new Long(2);
    private static final Long MAP_PROJECTION_ID = new Long(2);
    private static final long REPORT_ID = 1;

    @Test
    public void shouldConvertMapConfiguration() throws Exception {
        // given
        MapConfigurationType mapConfigurationType = new MapConfigurationType();
        mapConfigurationType.setScaleBarUnits(ScaleBarUnits.DEGREES);
        mapConfigurationType.setDisplayProjectionId(DISPLAY_PROJECTION_ID);
        mapConfigurationType.setSpatialConnectId(SPATIAL_CONNECT_ID);
        mapConfigurationType.setMapProjectionId(MAP_PROJECTION_ID);
        mapConfigurationType.setReportId(REPORT_ID);

        // when
        MapConfigurationDTO mapConfigurationDTO = MapConfigMapper.INSTANCE.mapConfigurationTypeToMapConfigurationDTO(mapConfigurationType);

        // then
        assertEquals(ScaleBarUnits.DEGREES.value(), mapConfigurationDTO.getScaleBarUnits());
        Assert.assertNull(mapConfigurationDTO.getCoordinatesFormat());
        assertEquals(DISPLAY_PROJECTION_ID, mapConfigurationDTO.getDisplayProjectionId());
        assertEquals(SPATIAL_CONNECT_ID, mapConfigurationDTO.getSpatialConnectId());
        assertEquals(MAP_PROJECTION_ID, mapConfigurationDTO.getMapProjectionId());
    }
}
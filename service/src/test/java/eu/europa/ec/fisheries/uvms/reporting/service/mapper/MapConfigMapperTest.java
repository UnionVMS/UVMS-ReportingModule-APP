/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
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
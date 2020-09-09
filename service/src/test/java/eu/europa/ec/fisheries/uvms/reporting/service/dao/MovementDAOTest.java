/*
 *
 *  Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2020.
 *
 *  This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 *  and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 *  the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 *  details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *  
 */

/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.dao;

import com.vividsolutions.jts.geom.LineString;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Movement;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Segment;
import eu.europa.ec.fisheries.uvms.reporting.service.util.GeometryUtil;
import lombok.SneakyThrows;
import org.junit.Test;
import static junit.framework.TestCase.assertEquals;

public class MovementDAOTest extends BaseMovementDAOTest {

    private MovementDao dao = new MovementDaoImpl(em);

    @Test
    @SneakyThrows
    public void testSaveMovement(){
        Movement movement = new Movement();
        movement.setMovementGuid("7913c585-3a20-4717-a045-a8db9633636f");
        movement.setSource("MANUAL");
        movement.setMovementType("POS");
        movement.setConnectionId("ee41a86b-6d1d-4b55-acc5-801c485a4cb6");
        movement.setCountryCode("EST");
        movement.setExternalMarking("AMA-028");
        movement.setName("AMA-028");
        movement.setIrcs("UNKNOWN");
        movement.setCfr("EST180525027");
        movement = dao.createEntity(movement);
        Movement savedMovement = dao.findById(movement.getId(),Movement.class);
        assertEquals(movement.getId(), savedMovement.getId());
    }
    
    @Test
    @SneakyThrows
    public void testSaveSegment(){
        Segment segment = new Segment();
        segment.setMovementGuid("7913c585-3a20-4717-a045-a8db9633636f");
        segment.setDuration(3654d);
        segment.setSpeedOverGround(32.0972778215656);
        segment.setCourseOverGround(217d);
        segment.setDistance(32.5787369611112);
        String wkt = "LINESTRING (12.241 57.107, 12.238 57.104, 12.235 57.101, 12.232 57.098, 12.229 57.095, 12.225999999999999 57.092,"
                + " 12.222999999999999 57.089, 12.219999999999999 57.086, 12.216999999999999 57.083, 12.213999999999999 57.08)";
        LineString lineString = GeometryUtil.toLineString(wkt);
        segment.setSegment(lineString);
        segment.setSegmentCategory("OTHER");
        segment.setCountryCode("EST");
        segment.setExternalMarking("AMA-028");
        segment.setName("AMA-028");
        segment.setIrcs("UNKNOWN");
        segment.setCfr("EST180525027");

        segment = dao.createEntity(segment);
        Segment savedSegment = dao.findById(segment.getId(),Segment.class);
        assertEquals(segment.getId(), savedSegment.getId());
    }

}
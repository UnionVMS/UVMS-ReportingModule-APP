
package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.schema.movement.v1.*;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselId;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.unitils.UnitilsJUnit4;
import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;


import java.net.URL;
import java.util.Arrays;
import java.util.GregorianCalendar;

import static junit.framework.TestCase.assertEquals;

public class VmsDTOTest extends UnitilsJUnit4 {

    private MovementMapResponseType movementMapResponseType;

    private Vessel vessel;

    @Test
    @SneakyThrows
    public void testToJsonEmpty(){

        URL url = Resources.getResource("payloads/VmsDTOTest.testToJsonEmpty.json");
        String expectedJSONString = Resources.toString(url, Charsets.UTF_8);

        VmsDTO dto = new VmsDTO(null, null);

        //assertEquals(expectedJSONString, prettify(dto.toJson()));

        JSONAssert.assertEquals(expectedJSONString, prettify(dto.toJson()), JSONCompareMode.STRICT);

    }

    @Before
    public void before(){

        vessel = new Vessel();
        vessel.setName("name");
        vessel.setVesselId(new VesselId());
        vessel.setCountryCode("BE");
        vessel.setIrcs("Ircs");
        vessel.setCfr("cfr");
        vessel.setActive(true);

        MovementType movement = new MovementType();
        movement.setCalculatedCourse(23.0);
        movement.setReportedSpeed(12.0);
        movement.setCalculatedSpeed(22.0);
        movement.setGuid("movementGuid");
        movement.setMovementType(MovementTypeType.EXI);
        MovementActivityType movementActivityType = new MovementActivityType();
        movementActivityType.setMessageType(MovementActivityTypeType.ANC);
        movement.setActivity(movementActivityType);
        movement.setPositionTime(DateUtils.dateToXmlGregorian(new GregorianCalendar(2013, 1, 28, 13, 24, 56).getTime()));
        movement.setWkt("POINT (30 10)");
        movement.setConnectId("guid");
        movement.setStatus("STATUS");

        movementMapResponseType = new MovementMapResponseType();
        movementMapResponseType.setKey("guid");
        movementMapResponseType.getMovements().add(movement);

        MovementSegment segment = new MovementSegment();
        segment.setTrackId("trackId");
        segment.setCategory(SegmentCategoryType.GAP);
        segment.setCourseOverGround(10.4);
        segment.setDistance(200.0);
        segment.setSpeedOverGround(13.5);
        segment.setDuration(100.0);
        segment.setWkt("LINESTRING (30 10, 10 30, 40 40)");

        movementMapResponseType.getSegments().add(segment);

        MovementTrack track = new MovementTrack();
        track.setTotalTimeAtSea(200.0);
        track.setDuration(212.0);
        track.setDistance(300.0);
        track.setWkt("MULTIPOLYGON (((40 40, 20 45, 45 30, 40 40)),\n" +
                "((20 35, 10 30, 10 10, 30 5, 45 20, 20 35),\n" +
                "(30 20, 20 15, 20 25, 30 20)))");
        movementMapResponseType.getTracks().add(track);

    }

    private String prettify(ObjectNode json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
    }

    @Test
    @SneakyThrows
    public void testToJsonHappy(){

        URL url = Resources.getResource("payloads/VmsDTOTest.testToJsonHappy.json");
        String expectedJSONString = Resources.toString(url, Charsets.UTF_8);

        VmsDTO dto = new VmsDTO(new ImmutableMap.Builder<String, Vessel>().put("guid", vessel).build(), Arrays.asList(movementMapResponseType));

        //assertEquals(expectedJSONString, prettify(dto.toJson()));

        assertJsonEquals(expectedJSONString, prettify(dto.toJson()));

    }

}
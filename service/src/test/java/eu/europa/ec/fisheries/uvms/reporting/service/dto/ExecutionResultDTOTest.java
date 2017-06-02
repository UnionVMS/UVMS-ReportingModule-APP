/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.schema.movement.v1.*;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingActivitySummary;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingTripIdWithGeometry;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.VesselIdentifierSchemeIdEnum;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.VesselIdentifierType;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetId;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.unitils.UnitilsJUnit4;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import static junit.framework.TestCase.assertEquals;
import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;

public class ExecutionResultDTOTest extends UnitilsJUnit4 {

    private TripDTO trip;

    private FishingActivitySummary activity;

    private MovementMapResponseType movementMapResponseType;

    private Asset asset;

    @Test
    @SneakyThrows
    public void testToJsonEmpty(){

        URL url = Resources.getResource("payloads/VmsDTOTest.testToJsonEmpty.json");
        String expectedJSONString = Resources.toString(url, Charsets.UTF_8);

        ExecutionResultDTO dto = new ExecutionResultDTO();

        //assertEquals(expectedJSONString, prettify(dto.toJson()));

        JSONAssert.assertEquals(expectedJSONString, prettify(dto.toJson(null)), JSONCompareMode.STRICT);

    }

    @Before
    public void before() throws DatatypeConfigurationException, ParseException {

        asset = new Asset();
        asset.setName("name");
        asset.setAssetId(new AssetId());
        asset.setCountryCode("BE");
        asset.setIrcs("Ircs");
        asset.setCfr("cfr");
        asset.setExternalMarking("em");
        asset.setActive(true);

        MovementType movement = new MovementType();
        movement.setReportedCourse(23.0);
        movement.setReportedSpeed(12.0);
        movement.setSource(MovementSourceType.AIS);
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

        trip = new TripDTO();
        trip.setTripId("TRIP1");
        trip.setSchemeId("SCHEME1");
        trip.setMultipointWkt("MULTIPOINT(10 20, 30 40)");

        activity = new FishingActivitySummary();
        activity.setGeometry("MULTIPOINT(10 20, 30 40)");
        activity.setActivityType("DEPARTURE");
        activity.setReportType("DECLARATION");
        activity.setAcceptedDateTime(getDate("2012-12-12 12:12:12"));
        activity.setDataSource("FLUX");
        activity.setPurposeCode("4");
        activity.setVesselName("VESSEL1");
        activity.setAreas(Arrays.asList("AREA1"));
        activity.setGears(Arrays.asList("GEAR1"));
        activity.setPorts(Arrays.asList("PORT1"));
        activity.setSpecies(Arrays.asList("SPECIES1"));
        activity.setVesselIdentifiers(Arrays.asList(new VesselIdentifierType(VesselIdentifierSchemeIdEnum.EXT_MARK,"VESSELID1")));

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

        ExecutionResultDTO dto = new ExecutionResultDTO();
        dto.setMovementMap(Arrays.asList(movementMapResponseType));
        dto.setAssetMap(new ImmutableMap.Builder<String, Asset>().put("guid", asset).build());
        dto.setTrips(Arrays.asList(trip));
        dto.setActivityList(Arrays.asList(activity));

        //assertEquals(expectedJSONString, prettify(dto.toJson(null)));

        assertJsonEquals(expectedJSONString, prettify(dto.toJson(null)));

    }

    private XMLGregorianCalendar getDate(String s) throws ParseException, DatatypeConfigurationException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        GregorianCalendar gregorianCalendar = (GregorianCalendar)GregorianCalendar.getInstance();
        gregorianCalendar.setTime(date);
        XMLGregorianCalendar result = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        return result;
    }

}
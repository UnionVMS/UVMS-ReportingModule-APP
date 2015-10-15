package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import eu.europa.ec.fisheries.schema.movement.v1.MovementActivityTypeType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.*;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Position;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Selector;
import lombok.SneakyThrows;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.reporting.service.dto.CommonFilterDTO.CommonFilterDTOBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.PositionSelectorDTO.PositionSelectorDTOBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.TrackFilterDTO.TrackFilterDTOBuild;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.VesselFilterDTO.VesselFilterDTOBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.VesselGroupFilterDTO.VesselGroupFilterDTOBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsPositionFilterDTO.VmsPositionFilterDTOBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsSegmentFilterDTO.VmsSegmentFilterDTOBuilder;
import static org.junit.Assert.assertEquals;

public class ReportDTOSerializerTest extends UnitilsJUnit4 {

    private static ObjectMapper mapper;

    private ReportDTO dto;

    @BeforeClass
    public static void beforeClass(){
       mapper = new ObjectMapper();
    }

    @Before
    public void before(){
        dto = ReportDTO.ReportDTOBuilder()
                .createdBy("georgi")
                .scopeName("356456731")
                .withMap(true)
                .filters(new ArrayList<FilterDTO>())
                .createdOn(DateUtils.stringToDate("2015-10-11 13:02:23 +0200"))
                .visibility(VisibilityEnum.PRIVATE)
                .description("This is a report descri created on 2015/09/28 13:31")
                .name("ReportName788")
                .isDeleted(false)
                .id(5L)
                .build();
    }

    @Test
    @SneakyThrows
    public void testSerializeWithoutFilters() {

        String serialized = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);

        URL url = Resources.getResource("payloads/ReportDTOSerializer.testSerializeWithoutFilters.json");
        String expected = Resources.toString(url, Charsets.UTF_8);

        assertEquals(expected, serialized);

    }

    @Test
    @SneakyThrows
    public void testSerializeWithoutFiltersWithoutDescription() {

        dto.setDescription(null);

        String serialized = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);
        URL url = Resources.getResource("payloads/ReportDTOSerializer.testSerializeWithoutFiltersWithoutDescription.json");
        String expected = Resources.toString(url, Charsets.UTF_8);

        assertEquals(expected, serialized);
    }

    @Test
    @SneakyThrows
    public void testSerializeWithFiltersWithVessel() {

        List<FilterDTO> filterDTOList = new ArrayList<>();
        filterDTOList.add(VesselFilterDTOBuilder().guid("guid1").name("vessel1").build());
        filterDTOList.add(VesselFilterDTOBuilder().id(48L).guid("guid2").name("vessel2").build());

        dto.setFilters(filterDTOList);

        String serialized = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);

        URL url = Resources.getResource("payloads/ReportDTOSerializer.testSerializeWithFiltersWithVessel.json");
        String expected = Resources.toString(url, Charsets.UTF_8);

        assertEquals(expected, serialized);
    }

    @Test
    @SneakyThrows
    public void testSerializeWithFiltersWithVesselAndVesselGroup() {

        List<FilterDTO> filterDTOList = new ArrayList<>();
        filterDTOList.add(VesselFilterDTOBuilder().guid("guid1").name("vessel1").build());
        filterDTOList.add(VesselFilterDTOBuilder().id(48L).guid("guid2").name("vessel2").build());
        filterDTOList.add(VesselGroupFilterDTOBuilder().name("name2").guid("guid6").id(66L).userName("houston").build());
        filterDTOList.add(VesselGroupFilterDTOBuilder().name("name2").guid("guid7").id(67L).userName("houstonGreg").build());

        dto.setFilters(filterDTOList);

        String serialized = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);

        URL url = Resources.getResource("payloads/ReportDTOSerializer.testSerializeWithFiltersWithVesselAndVesselGroup.json");
        String expected = Resources.toString(url, Charsets.UTF_8);

        assertEquals(expected, serialized);
    }

    @Test
    @SneakyThrows
    public void testSerializeWithFiltersWithCommonFilterWithSelectorAll() {

        List<FilterDTO> filterDTOList = new ArrayList<>();
        filterDTOList.add(CommonFilterDTOBuilder()
                .endDate(new DateTime(2004, 3, 26, 12, 1, 1, 1).toDate())
                .startDate(new DateTime(2005, 3, 26, 12, 1, 1, 1).toDate())
                .positionSelector(PositionSelectorDTOBuilder().selector(Selector.ALL).build())
                .build());

        dto.setFilters(filterDTOList);

        String serialized = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);

        URL url = Resources.getResource("payloads/ReportDTOSerializer.testSerializeWithFiltersWithCommonFilterWithSelectorAll.json");
        String expected = Resources.toString(url, Charsets.UTF_8);

        assertEquals(expected, serialized);
    }

    @Test
    @SneakyThrows
    public void testSerializeWithFiltersWithCommonFilterWithSelectorLastHours() {

        List<FilterDTO> filterDTOList = new ArrayList<>();
        filterDTOList.add(CommonFilterDTOBuilder()
                .positionSelector(PositionSelectorDTOBuilder()
                        .selector(Selector.LAST)
                        .position(Position.hours)
                        .value(23.45F)
                        .build())
                .build());

        dto.setFilters(filterDTOList);

        String serialized = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);

        URL url = Resources.getResource("payloads/ReportDTOSerializer.testSerializeWithFiltersWithCommonFilterWithSelectorLastHours.json");
        String expected = Resources.toString(url, Charsets.UTF_8);

        assertEquals(expected, serialized);
    }

    @Test
    @SneakyThrows
    public void testSerializeWithFiltersWithCommonFilterWithSelectorLastPositions() {

        List<FilterDTO> filterDTOList = new ArrayList<>();
        filterDTOList.add(CommonFilterDTOBuilder()
                .positionSelector(PositionSelectorDTOBuilder()
                        .selector(Selector.LAST)
                        .position(Position.positions)
                        .value(23F)
                        .build())
                .build());

        dto.setFilters(filterDTOList);

        String serialized = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);

        URL url = Resources.getResource("payloads/ReportDTOSerializer.testSerializeWithFiltersWithCommonFilterWithSelectorLastPositions.json");
        String expected = Resources.toString(url, Charsets.UTF_8);

        assertEquals(expected, serialized);
    }

    @Test
    @SneakyThrows
    public void testSerializeWithFiltersWithCommonFilterWithSelectorLastPositionsWithStartDate() {

        List<FilterDTO> filterDTOList = new ArrayList<>();
        filterDTOList.add(CommonFilterDTOBuilder()
                .startDate(new DateTime(2004, 3, 26, 12, 1, 1, 1).toDate())
                .positionSelector(PositionSelectorDTOBuilder()
                        .selector(Selector.LAST)
                        .position(Position.positions)
                        .value(23F)
                        .build())
                .build());

        dto.setFilters(filterDTOList);

        String serialized = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);

        URL url = Resources
                .getResource("payloads/ReportDTOSerializer.testSerializeWithFiltersWithCommonFilterWithSelectorLastPositionsWIthStartDate.json");
        String expected = Resources.toString(url, Charsets.UTF_8);

        assertEquals(expected, serialized);
    }

    @Test
    @SneakyThrows
    public void testSerializeWithFiltersWithCommonFilterWithTracks() {

        List<FilterDTO> filterDTOList = new ArrayList<>();
        filterDTOList.add(TrackFilterDTOBuild()
                .id(1L)
                .maxDuration(200.345F)
                .maxTime(20.345F)
                .minDuration(40.5F)
                .minTime(10F)
                .build());

        dto.setFilters(filterDTOList);

        String serialized = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);

        URL url = Resources.getResource("payloads/ReportDTOSerializer.testSerializeWithFiltersWithTracks.json");
        String expected = Resources.toString(url, Charsets.UTF_8);

        assertEquals(expected, serialized);
    }

    @Test
    @SneakyThrows
    public void testSerializeWithFiltersWithVmsPositions() {

        List<FilterDTO> filterDTOList = new ArrayList<>();
        filterDTOList.add(VmsPositionFilterDTOBuilder()
                .id(5L)
                .maximumSpeed(234.2F)
                .minimumSpeed(45.5F)
                .movementType(MovementTypeType.EXI)
                .movementActivity(MovementActivityTypeType.CAN)
                .build());

        dto.setFilters(filterDTOList);

        String serialized = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);

        URL url = Resources.getResource("payloads/ReportDTOSerializer.testSerializeWithFiltersWithVmsPositions.json");
        String expected = Resources.toString(url, Charsets.UTF_8);

        assertEquals(expected, serialized);
    }

    @Test
    @SneakyThrows
    public void testSerializeWithFiltersWithVmsPositionsWithoutSomeFields() {

        List<FilterDTO> filterDTOList = new ArrayList<>();
        filterDTOList.add(VmsPositionFilterDTOBuilder()
                .id(5L)
                .movementType(MovementTypeType.EXI)
                .movementActivity(MovementActivityTypeType.CAN)
                .build());

        dto.setFilters(filterDTOList);

        String serialized = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);

        URL url = Resources.getResource("payloads/ReportDTOSerializer.testSerializeWithFiltersWithVmsPositionsWithoutSomeFields.json");
        String expected = Resources.toString(url, Charsets.UTF_8);

        assertEquals(expected, serialized);
    }

    @Test
    @SneakyThrows
    public void testSerializeWithFiltersWithVmsSegments() {

        List<FilterDTO> filterDTOList = new ArrayList<>();
        filterDTOList.add(VmsPositionFilterDTOBuilder()
                .id(5L)
                .maximumSpeed(234.2F)
                .minimumSpeed(45.5F)
                .movementType(MovementTypeType.EXI)
                .movementActivity(MovementActivityTypeType.CAN)
                .build());

        filterDTOList.add(VmsSegmentFilterDTOBuilder()
                .id(5L)
                .maximumSpeed(234.2F)
                .minimumSpeed(45.5F)
                .build());

        dto.setFilters(filterDTOList);

        String serialized = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);

        URL url = Resources.getResource("payloads/ReportDTOSerializer.testSerializeWithFiltersWithVmsSegments.json");
        String expected = Resources.toString(url, Charsets.UTF_8);

        assertEquals(expected, serialized);
    }


    @Test
    @SneakyThrows
    public void testSerializeWithAreaFilters() {

        List<FilterDTO> filterDTOList = new ArrayList<>();
        filterDTOList.add(AreaFilterDTO.AreaFilterDTOBuilder().areaId(345364L).areaType("eez").build());
        filterDTOList.add(AreaFilterDTO.AreaFilterDTOBuilder().id(48L).areaId(42524L).areaType("user").build());
        filterDTOList.add(AreaFilterDTO.AreaFilterDTOBuilder().id(66L).areaId(21312L).areaType("eez").build());
        filterDTOList.add(AreaFilterDTO.AreaFilterDTOBuilder().id(67L).areaId(56546L).areaType("rfmo").build());

        dto.setFilters(filterDTOList);

        String serialized = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);

        URL url = Resources.getResource("payloads/ReportDTOSerializer.testSerializeWithAreaFilters.json");
        String expected = Resources.toString(url, Charsets.UTF_8);

        assertEquals(expected, serialized);
    }
}

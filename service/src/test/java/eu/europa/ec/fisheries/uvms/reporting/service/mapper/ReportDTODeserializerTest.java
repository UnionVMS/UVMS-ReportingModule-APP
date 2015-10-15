package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import eu.europa.ec.fisheries.schema.movement.v1.MovementActivityTypeType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsPositionFilterDTO;
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
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsPositionFilterDTO.VmsPositionFilterDTOBuilder;
import static junit.framework.TestCase.assertTrue;

@Ignore
public class ReportDTODeserializerTest extends UnitilsJUnit4 {

    private static ObjectMapper mapper;

    private ReportDTO dto;

    @BeforeClass
    public static void beforeClass(){
        mapper = new ObjectMapper();
    }

    @Before
    public void before(){
        dto = ReportDTO.ReportDTOBuilder()
                .withMap(true)
                .visibility(VisibilityEnum.PRIVATE)
                .description("This is a report descri created on 2015/09/28 13:31")
                .name("ReportName788")
                .isDeleted(false)
                .filters(new ArrayList<FilterDTO>())
                .id(5L)
                .build();
    }

    @Test
    @SneakyThrows
    public void testSerializeWithoutFilters() {

        URL url = Resources.getResource("payloads/ReportDTOSerializer.testSerializeWithoutFilters.json");
        String expected = Resources.toString(url, Charsets.UTF_8);

        ReportDTO deserialized = mapper.readValue(expected, ReportDTO.class);

        assertTrue(deserialized.equals(dto));
    }

    @Test
    @SneakyThrows
    public void testSerializeWithFiltersWithVmsPositionsWithoutSomeFields() {

        List<FilterDTO> filterDTOList = new ArrayList<>();
        filterDTOList.add(VmsPositionFilterDTOBuilder()
                .movementType(MovementTypeType.EXI)
                .movementActivity(MovementActivityTypeType.CAN)
                .id(5L)
                .build());
            dto.setFilters(filterDTOList);

        URL url = Resources.
                getResource("payloads/ReportDTOSerializer." +
                        "testSerializeWithFiltersWithVmsPositionsWithoutSomeFields.json");
        String expected = Resources.toString(url, Charsets.UTF_8);

        ReportDTO deserialized = mapper.readValue(expected, ReportDTO.class);

        assertTrue(deserialized.equals(dto));
    }

    @Test
    @SneakyThrows
    public void testSerializeWithFiltersWithVmsSegments() {

        List<FilterDTO> filterDTOList = new ArrayList<>();


        URL url = Resources.
                getResource("payloads/ReportDTOSerializer.testSerializeWithFiltersWithVmsSegments.json");
        String expected = Resources.toString(url, Charsets.UTF_8);

        ReportDTO deserialized = mapper.readValue(expected, ReportDTO.class);

        assertTrue(deserialized.equals(dto));
    }


}

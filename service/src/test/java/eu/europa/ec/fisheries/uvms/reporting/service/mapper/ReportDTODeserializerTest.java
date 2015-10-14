package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;

import java.net.URL;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

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
                .createdBy("georgi")
                .scopeName("356456731")
                .withMap(true)
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

        URL url = Resources.getResource("ReportDTOSerializer.testSerializeWithFiltersWithCommonFilterWithSelectorAll.json");
        String expected = Resources.toString(url, Charsets.UTF_8);

        ReportDTO deserialised = mapper.readValue(expected, ReportDTO.class);

        assertTrue(deserialised.equals(dto));
    }

}

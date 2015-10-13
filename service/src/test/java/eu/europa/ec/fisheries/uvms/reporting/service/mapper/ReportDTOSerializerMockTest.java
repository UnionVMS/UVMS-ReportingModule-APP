package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import eu.europa.ec.fisheries.schema.movement.v1.MovementActivityTypeType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.CommonFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.PositionSelectorDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VesselFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VesselGroupFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsPositionFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Selector;
import lombok.SneakyThrows;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static eu.europa.ec.fisheries.uvms.common.DateUtils.UI_FORMATTER;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.CommonFilterDTO.CommonFilterDTOBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.PositionSelectorDTO.PositionSelectorDTOBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.VesselFilterDTO.VesselFilterDTOBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.VesselGroupFilterDTO.VesselGroupFilterDTOBuilder;

public class ReportDTOSerializerMockTest extends UnitilsJUnit4 {

    @TestedObject
    private ReportDTOSerializer serializer;

    private ReportDTO dto;

    private Mock<JsonGenerator> gen;

    @Before
    public void beforeTest(){
        dto = ReportDTO.ReportDTOBuilder()
                .createdBy("georgi")
                .scopeName("356456731")
                .withMap(true)
                .createdOn(DateUtils.stringToDate("2015-10-11 13:02:23 +0200"))
                .visibility(VisibilityEnum.PRIVATE)
                .description("This is a report descri created on 2015/09/28 13:31")
                .name("ReportName788")
                .filters(null)
                .isDeleted(false)
                .id(5L)
                .build();
    }

    @Test
    @SneakyThrows
    public void testSerializeList(){

        serializer.serialize(dto, gen.getMock(), null);

        commonFields();

        gen.assertInvoked().writeBooleanField(ReportDTO.SHAREABLE, false);
        gen.assertInvoked().writeBooleanField(ReportDTO.DELETABLE, false);
        gen.assertInvoked().writeBooleanField(ReportDTO.EDITABLE, false);

        gen.assertNotInvoked().writeFieldName(ReportDTO.FILTER_EXPRESSION);
        gen.assertNotInvoked().writeFieldName(CommonFilterDTO.COMMON);
        gen.assertNotInvoked().writeFieldName(VesselFilterDTO.VESSEL);
        gen.assertNotInvoked().writeFieldName(VmsPositionFilterDTO.VMS);

    }

    private void commonFields() throws IOException {
        gen.assertInvoked().writeNumberField(ReportDTO.ID, dto.getId());
        gen.assertInvoked().writeStringField(ReportDTO.NAME, dto.getName());
        gen.assertInvoked().writeStringField(ReportDTO.DESC, dto.getDescription());
        gen.assertInvoked().writeStringField(ReportDTO.VISIBILITY, dto.getVisibility().getName());
        gen.assertInvoked().writeStringField(ReportDTO.CREATED_ON,
                UI_FORMATTER.print(new DateTime(dto.getAudit().getCreatedOn())));
        gen.assertInvoked().writeStringField(ReportDTO.CREATED_BY, dto.getCreatedBy());
        gen.assertNotInvoked().writeStringField(ReportDTO.SCOPE_ID, dto.getScopeName());
        gen.assertInvoked().writeBooleanField(ReportDTO.WITH_MAP, dto.getWithMap());
    }

    @Test
    @SneakyThrows
    public void testSerializeDetail(){

        List<FilterDTO> filterDTOList = new ArrayList<>();

        VesselFilterDTO vessel1 = VesselFilterDTOBuilder().id(47L).guid("guid1").name("vessel1").build();

        VesselGroupFilterDTO vgroup2 = VesselGroupFilterDTOBuilder().id(48L).guid("2").userName("ddd").build();

        VesselFilterDTO vessel2 = VesselFilterDTOBuilder().id(49L).guid("gui4564").name("vessel2").build();

        VesselGroupFilterDTO vgroup1 = VesselGroupFilterDTOBuilder().id(45L).guid("1").userName("ffsdfs").build();

        VmsPositionFilterDTO vms = VmsPositionFilterDTO.VmsPositionFilterDTOBuilder().id(1L)
                .movementActivity(MovementActivityTypeType.ANC)
                .minimumSpeed(100F)
                .maximumSpeed(123F)
                .movementType(MovementTypeType.ENT)
                .build();

        CommonFilterDTO common = CommonFilterDTOBuilder()
                .id(46L)
                .positionSelector(PositionSelectorDTOBuilder().selector(Selector.ALL).build())
                .endDate(DateUtils.stringToDate("2015-10-09 08:56:48 +0200"))
                .startDate(DateUtils.stringToDate("2015-10-09 08:56:48 +0200"))
                .build();


        filterDTOList.add(vgroup1);
        filterDTOList.add(vgroup2);
        filterDTOList.add(vessel1);
        filterDTOList.add(vessel2);
        filterDTOList.add(common);
        filterDTOList.add(vms);

        dto.setFilters(filterDTOList);

        serializer.serialize(dto, gen.getMock(), null);

        commonFields();

        gen.assertNotInvoked().writeBooleanField(ReportDTO.SHAREABLE, false);
        gen.assertNotInvoked().writeBooleanField(ReportDTO.DELETABLE, false);
        gen.assertNotInvoked().writeBooleanField(ReportDTO.EDITABLE, false);

        gen.assertInvoked().writeFieldName(ReportDTO.FILTER_EXPRESSION);
        gen.assertInvoked().writeFieldName(CommonFilterDTO.COMMON);
        gen.assertInvoked().writeFieldName(VesselFilterDTO.VESSEL);
        gen.assertInvoked().writeFieldName(VmsPositionFilterDTO.VMS);

        gen.assertInvoked().writeNumberField(FilterDTO.ID, common.getId());
        gen.assertInvoked().writeStringField(CommonFilterDTO.POSITION_SELECTOR,
                Selector.ALL.getName());

        gen.assertInvoked().writeStringField(
                CommonFilterDTO.START_DATE, UI_FORMATTER.print(new DateTime(common.getStartDate())));
        gen.assertInvoked().writeStringField(
                CommonFilterDTO.END_DATE, UI_FORMATTER.print(new DateTime(common.getEndDate())));

        gen.assertNotInvoked().writeNumberField(PositionSelectorDTO.VALUE, null);
        gen.assertNotInvoked().writeStringField(CommonFilterDTO.POSITION_SELECTOR,
                Selector.LAST.getName());

    }
}

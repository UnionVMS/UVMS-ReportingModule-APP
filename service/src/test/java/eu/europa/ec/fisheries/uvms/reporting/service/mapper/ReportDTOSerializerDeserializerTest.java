/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import eu.europa.ec.fisheries.schema.movement.v1.MovementActivityTypeType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.schema.movement.v1.SegmentCategoryType;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.*;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Position;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Selector;
import io.jsonwebtoken.lang.Collections;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lombok.SneakyThrows;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.reporting.service.dto.CommonFilterDTO.CommonFilterDTOBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.PositionSelectorDTO.PositionSelectorDTOBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO.ReportDTOBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.AssetFilterDTO.AssetFilterDTOBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.AssetGroupFilterDTO.AssetGroupFilterDTOBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsPositionFilterDTO.VmsPositionFilterDTOBuilder;
import static junit.framework.TestCase.assertTrue;
import static junitparams.JUnitParamsRunner.$;

@RunWith(JUnitParamsRunner.class)
public class ReportDTOSerializerDeserializerTest {

    private static ObjectMapper mapper;

    @BeforeClass
    public static void beforeClass(){
        mapper = new ObjectMapper();
    }

    @Test
    @SneakyThrows
    @Parameters(method = "payloadValues")
    public void shouldBeEqual(ReportDTO report, String payload) {

        String serialized = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(report);

        String expectedJSONString = Resources.toString(Resources.getResource(payload), Charsets.UTF_8);

        JSONAssert.assertEquals(expectedJSONString, serialized, JSONCompareMode.LENIENT);

        assertTrue(mapper.readValue(expectedJSONString, ReportDTO.class).equals(report));

    }

    protected Object[] payloadValues(){

        ReportDTO report1 = createReport();
        report1.setWithMap(true);
        report1.setMapConfiguration(new MapConfigurationDTO());
        report1.setShareable(Collections.arrayToList(new VisibilityEnum[]{VisibilityEnum.PRIVATE}));

        ReportDTO report2 = createReport();
        report2.setDescription(null);
        report2.setShareable(Collections.arrayToList(new VisibilityEnum[]{VisibilityEnum.PRIVATE, VisibilityEnum.SCOPE}));

        ReportDTO report3 = createReport();
        List<FilterDTO> filterDTOList = new ArrayList<>();
        filterDTOList.add(AssetFilterDTOBuilder().guid("guid1").name("asset1").build());
        filterDTOList.add(AssetFilterDTOBuilder().id(48L).guid("guid2").name("asset2").build());
        report3.setFilters(filterDTOList);

        ReportDTO report4 = createReport();
        filterDTOList = new ArrayList<>();
        filterDTOList.add(AssetFilterDTOBuilder().guid("guid1").name("asset1").build());
        filterDTOList.add(AssetFilterDTOBuilder().id(48L).guid("guid2").name("asset2").build());
        filterDTOList.add(AssetGroupFilterDTOBuilder().name("name2").guid("guid6").id(66L).userName("houston").build());
        filterDTOList.add(AssetGroupFilterDTOBuilder().name("name2").guid("guid7").id(67L).userName("houstonGreg").build());
        report4.setFilters(filterDTOList);

        ReportDTO report5 = createReport();
        filterDTOList = new ArrayList<>();
        Calendar calendar = new GregorianCalendar(2013,1,28,13,24,56);
        filterDTOList.add(CommonFilterDTOBuilder().endDate(calendar.getTime()).startDate(calendar.getTime())
                .positionSelector(PositionSelectorDTOBuilder().selector(Selector.all).build()).build());
        report5.setFilters(filterDTOList);

        ReportDTO report6 = createReport();
        filterDTOList = new ArrayList<>();
        filterDTOList.add(CommonFilterDTOBuilder().positionSelector(PositionSelectorDTOBuilder()
                        .selector(Selector.last).position(Position.hours).value(23.45F).build()).build());
        report6.setFilters(filterDTOList);

        ReportDTO report7 = createReport();
        filterDTOList = new ArrayList<>();
        filterDTOList.add(CommonFilterDTOBuilder().positionSelector(PositionSelectorDTOBuilder()
                        .selector(Selector.last).position(Position.positions).value(23F).build()).build());
        report7.setFilters(filterDTOList);

        ReportDTO report8 = createReport();
        filterDTOList = new ArrayList<>();
        calendar = new GregorianCalendar(2013,1,28,13,24,0);
        filterDTOList.add(CommonFilterDTOBuilder().startDate(calendar.getTime())
                .positionSelector(PositionSelectorDTOBuilder().selector(Selector.last).position(Position.positions)
                        .value(23F).build()).build());
        report8.setFilters(filterDTOList);

        ReportDTO report9 = createReport();
        filterDTOList = new ArrayList<>();
        filterDTOList.add(TrackFilterDTO.builder().id(1L).maxDuration(200.345F).maxTime(20.345F).minDuration(40.5F)
                .minTime(10F).build());
        report9.setFilters(filterDTOList);

        ReportDTO report10 = createReport();
        filterDTOList = new ArrayList<>();
        filterDTOList.add(VmsPositionFilterDTOBuilder().id(5L).maximumSpeed(234.2F).minimumSpeed(45.5F)
                .movementType(MovementTypeType.EXI).movementActivity(MovementActivityTypeType.CAN).build());
        report10.setFilters(filterDTOList);

        ReportDTO report11 = createReport();
        filterDTOList = new ArrayList<>();
        filterDTOList.add(VmsPositionFilterDTOBuilder().id(5L).movementType(MovementTypeType.EXI)
                .movementActivity(MovementActivityTypeType.CAN).build());
        report11.setFilters(filterDTOList);

        ReportDTO report12 = createReport();
        filterDTOList = new ArrayList<>();
        filterDTOList.add(VmsPositionFilterDTOBuilder().id(5L).maximumSpeed(234.2F).minimumSpeed(45.5F)
                .movementType(MovementTypeType.EXI).movementActivity(MovementActivityTypeType.CAN).build());
        filterDTOList.add(VmsSegmentFilterDTO.builder().id(5L).maximumSpeed(234.2F).minimumSpeed(45.5F)
                .minDuration(33.3F).maxDuration(33.3f).category(SegmentCategoryType.ENTER_PORT).build());
        report12.setFilters(filterDTOList);

        ReportDTO report13 = createReport();
        filterDTOList = new ArrayList<>();
        filterDTOList.add(AreaFilterDTO.AreaFilterDTOBuilder().areaId(345364L).areaType("eez").build());
        filterDTOList.add(AreaFilterDTO.AreaFilterDTOBuilder().id(48L).areaId(42524L).areaType("user").build());
        filterDTOList.add(AreaFilterDTO.AreaFilterDTOBuilder().id(66L).areaId(21312L).areaType("eez").build());
        filterDTOList.add(AreaFilterDTO.AreaFilterDTOBuilder().id(67L).areaId(56546L).areaType("rfmo").build());
        report13.setFilters(filterDTOList);

        return $(
                $(report1, "payloads/ReportDTOSerializerDeserializerTest.testWithoutFilters.json"),
                $(report2, "payloads/ReportDTOSerializerDeserializerTest.testWithoutFiltersWithoutDescription.json"),
                $(report3, "payloads/ReportDTOSerializerDeserializerTest.testWithFiltersWithAsset.json"),
                $(report4, "payloads/ReportDTOSerializerDeserializerTest.testWithFiltersWithAssetAndAssetGroup.json"),
                $(report5, "payloads/ReportDTOSerializerDeserializerTest.testWithFiltersWithCommonFilterWithSelectorAll.json"),
                $(report6, "payloads/ReportDTOSerializerDeserializerTest.testWithFiltersWithCommonFilterWithSelectorLastHours.json"),
                $(report7, "payloads/ReportDTOSerializerDeserializerTest.testWithFiltersWithCommonFilterWithSelectorLastPositions.json"),
                $(report8, "payloads/ReportDTOSerializerDeserializerTest.testWithFiltersWithCommonFilterWithSelectorLastPositionsWithStartDate.json"),
                $(report9, "payloads/ReportDTOSerializerDeserializerTest.testWithFiltersWithTracks.json"),
                $(report10, "payloads/ReportDTOSerializerDeserializerTest.testWithFiltersWithVmsPositions.json"),
                $(report11, "payloads/ReportDTOSerializerDeserializerTest.testWithFiltersWithVmsPositionsWithoutSomeFields.json"),
                $(report12, "payloads/ReportDTOSerializerDeserializerTest.testWithFiltersWithVmsSegmentsVmsPosition.json"),
                $(report13, "payloads/ReportDTOSerializerDeserializerTest.testWithAreaFilters.json")
        );
    }

    private ReportDTO createReport() {
        return ReportDTOBuilder().createdBy("georgi").scopeName("356456731").withMap(false)
                .filters(new ArrayList<FilterDTO>()).createdOn(DateUtils.stringToDate("2015-10-11 13:02:23 +0200"))
                .visibility(VisibilityEnum.PRIVATE)
                .description("This is a report descri created on 2015/09/28 13:31")
                .name("ReportName788").isDeleted(false).id(5L).build();
    }
}
package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.CommonFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.CommonFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.DateRange;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertEquals;

@RunWith(JUnitParamsRunner.class)
public class CommonFilterMapperTest {

    @Test
    @Parameters(method = "filterValues")
    public void shouldConvertToDto(CommonFilter filter, CommonFilterDTO expectedResult){

        CommonFilterDTO commonFilterDTO = CommonFilterMapper.INSTANCE.dateTimeFilterToDateTimeFilterDTO(filter);
        assertEquals(expectedResult, commonFilterDTO);

    }

    protected Object[] filterValues(){

        Calendar instance = Calendar.getInstance();
        Date start = instance.getTime();
        Date end = instance.getTime();

        CommonFilter commonFilter1 = CommonFilter.builder().dateRange(new DateRange(null, null)).build();
        CommonFilterDTO expected1 = new CommonFilterDTO();

        CommonFilter commonFilter2 = CommonFilter.builder().dateRange(new DateRange(start, null)).build();
        CommonFilterDTO expected2 = new CommonFilterDTO();
        expected2.setStartDate(start);

        CommonFilter commonFilter3 = CommonFilter.builder().dateRange(new DateRange(null, end)).build();
        CommonFilterDTO expected3 = new CommonFilterDTO();
        expected3.setEndDate(end);

        CommonFilter commonFilter4 = CommonFilter.builder().dateRange(new DateRange(start, end)).build();
        CommonFilterDTO expected4 = new CommonFilterDTO();
        expected4.setStartDate(start);
        expected4.setEndDate(end);

        return $(
                $(commonFilter1, expected1),
                $(commonFilter2, expected2),
                $(commonFilter3, expected3),
                $(commonFilter4, expected4)
        );
    }
}

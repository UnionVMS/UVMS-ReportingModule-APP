/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.domain.DateRange;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.CommonFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.CommonFilter;
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
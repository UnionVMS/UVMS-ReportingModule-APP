/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.vividsolutions.jts.util.Assert;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ExecutionLog;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportDetails;
import lombok.SneakyThrows;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.HashSet;
import java.util.Set;

public class ReportMapperV2Test {

    private static ObjectMapper mapper;

    @BeforeClass
    public static void beforeClass(){
        mapper = new ObjectMapper();
    }

    @Test
    @SneakyThrows
    public void testReportDtoToReport(){

        String expectedJSONString = Resources.toString(Resources.getResource("payloads/ReportMapperV2Test.json"), Charsets.UTF_8);

        Report report = ReportMapperV2.INSTANCE.reportDtoToReport(mapper.readValue(expectedJSONString, eu.europa.ec.fisheries.uvms.reporting.model.vms.Report.class));

        assertNotNull(report);

        Set<Filter> filters = report.getFilters();

        assertEquals(9, filters.size());

    }
}
/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import static org.junit.Assert.assertFalse;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.report.Report;
import org.junit.Test;

public class ReportMapperV2Test {

    @Test
    public void testReportDtoToReport() throws IOException {

        String json = "{\n" + "    \"name\": \"test\",\n" + "    \"desc\": null,\n" + "    \"reportType\": \"standard\",\n" + "    \"withMap\": true,\n" + "    \"visibility\": \"private\",\n" + "    \"filterExpression\": {\n" + "        \"common\": {\n" + "            \"startDate\": \"2017-05-01 13:55:38 +0000\",\n" + "            \"endDate\": \"2017-08-02 13:55:38 +0000\",\n" + "            \"positionTypeSelector\": \"positions\",\n" + "            \"positionSelector\": \"all\"\n" + "        },\n" + "        \"areas\": []\n" + "    },\n" + "    \"mapConfiguration\": {},\n" + "    \"additionalProperties\": {\n" + "        \"speedUnit\": \"kts\",\n" + "        \"distanceUnit\": \"nm\",\n" + "        \"timestamp\": \"2018-05-23 18:21:52 +0000\"\n" + "    }\n" + "}";
        ObjectMapper mapper = new ObjectMapper();
        Report report = mapper.readValue(json, Report.class);

        eu.europa.ec.fisheries.uvms.reporting.service.entities.Report entity = ReportMapperV2.INSTANCE.reportDtoToReport(report);

        assertFalse(entity.isLastPositionSelected());
    }
}

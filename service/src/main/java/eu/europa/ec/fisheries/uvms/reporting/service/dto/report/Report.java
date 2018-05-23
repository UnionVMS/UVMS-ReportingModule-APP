/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.service.dto.report;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterExpression;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MapConfiguration;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
@EqualsAndHashCode
@Data
public class Report {

    private Long id;
    private String name;
    private String desc;
    private String visibility;
    private String reportType;
    private String createdOn;
    private Object executedOn;
    private String createdBy;
    private boolean withMap;
    private MapConfiguration mapConfiguration;
    private FilterExpression filterExpression;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public String getReportType() {
        if (StringUtils.isNotEmpty(reportType)){
            return StringUtils.upperCase(reportType);
        }
        return null;
    }

}
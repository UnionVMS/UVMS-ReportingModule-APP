/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europa.ec.fisheries.uvms.reporting.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.mapper.AreaFilterMapper;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true, of = {"areaType", "areaId"})
public class AreaFilterDTO extends FilterDTO {

    public static final String JSON_ATTR_AREA_TYPE = "areaType";
    public static final String JSON_ATTR_AREA_ID = "gid";

    @NotNull
    private String areaType;

    @NotNull
    @JsonProperty(JSON_ATTR_AREA_ID)
    private Long areaId;

    public AreaFilterDTO() {
        super(FilterType.areas);
    }

    public AreaFilterDTO(Long id, Long reportId) {
        super(FilterType.areas, id, reportId);
    }

    @Builder(builderMethodName = "AreaFilterDTOBuilder")
    public AreaFilterDTO(Long reportId, Long id, Long areaId, String areaType) {
        this(id, reportId);
        this.areaId = areaId;
        this.areaType = areaType;
        validate();
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    @Override
    public Filter convertToFilter() {
        return AreaFilterMapper.INSTANCE.areaFilterDTOToAreaFilter(this);
    }

}
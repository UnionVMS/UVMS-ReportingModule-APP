/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europa.ec.fisheries.uvms.reporting.service.type.AreaTypeEnum;

/**
 * Created by padhyad on 1/11/2016.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LayerAreaDto extends LayersDto {

    @JsonProperty("areaType")
    private AreaTypeEnum areaType;

    @JsonProperty("gid")
    private Long gid;

    @JsonProperty("areaName")
    private String areaName;

    @JsonProperty("desc")
    private String areaDesc;

    @JsonProperty("areaGroupName")
    private String areaGroupName;

    @JsonProperty("areaType")
    public AreaTypeEnum getAreaType() {
        return areaType;
    }

    @JsonProperty("areaType")
    public void setAreaType(AreaTypeEnum areaType) {
        this.areaType = areaType;
    }

    @JsonProperty("gid")
    public Long getGid() {
        return gid;
    }

    @JsonProperty("gid")
    public void setGid(Long gid) {
        this.gid = gid;
    }

    @JsonProperty("areaName")
    public String getAreaName() {
        return areaName;
    }

    @JsonProperty("areaName")
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    @JsonProperty("desc")
    public String getAreaDesc() {
        return areaDesc;
    }

    @JsonProperty("desc")
    public void setAreaDesc(String areaDesc) {
        this.areaDesc = areaDesc;
    }

    @JsonProperty("areaGroupName")
    public String getAreaGroupName() {
        return areaGroupName;
    }

    @JsonProperty("areaGroupName")
    public void setAreaGroupName(String areaGroupName) {
        this.areaGroupName = areaGroupName;
    }

}
/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode
@ToString
public class FilterExpression {

    @JsonProperty("common")
    @Valid
    private DateTime common;

    @JsonProperty("vms")
    @Valid
    private Vms vms;

    @JsonProperty("areas")
    @Valid
    private List<Area> areas = new ArrayList<Area>();

    @JsonProperty("assets")
    @Valid
    private List<Asset> assets = new ArrayList<Asset>();

    @JsonProperty("fa")
    private FaFilter faFilter;

    @JsonProperty("criteria")
    private CriteriaFilterDTO criteriaFilter;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public FilterExpression() {
    }

    public FilterExpression(DateTime common, Vms vms, List<Area> areas, List<Asset> assets) {
        this.common = common;
        this.vms = vms;
        this.areas = areas;
        this.assets = assets;
    }

    @JsonProperty("common")
    public DateTime getCommon() {
        return common;
    }

    @JsonProperty("common")
    public void setCommon(DateTime common) {
        this.common = common;
    }

    @JsonProperty("vms")
    public Vms getVms() {
        return vms;
    }

    @JsonProperty("vms")
    public void setVms(Vms vms) {
        this.vms = vms;
    }

    @JsonProperty("areas")
    public List<Area> getAreas() {
        return areas;
    }

    @JsonProperty("areas")
    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    @JsonProperty("assets")
    public List<Asset> getAssets() {
        return assets;
    }

    @JsonProperty("assets")
    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    @JsonProperty("fa")
    public FaFilter getFaFilter() {
        return faFilter;
    }

    @JsonProperty("fa")
    public void setFaFilter(FaFilter faFilter) {
        this.faFilter = faFilter;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public CriteriaFilterDTO getCriteriaFilter() {
        return criteriaFilter;
    }

    public void setCriteriaFilter(CriteriaFilterDTO criteriaFilter) {
        this.criteriaFilter = criteriaFilter;
    }

}
/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LayerSettingsDto implements Serializable{

    @JsonProperty("baseLayers")
    private List<LayersDto> baseLayers;

    @JsonProperty("portLayers")
    private List<LayersDto> portLayers;

    @JsonProperty("additionalLayers")
    private List<LayersDto> additionalLayers;

    @JsonProperty("areaLayers")
    private List<LayerAreaDto> areaLayers;

    public LayerSettingsDto() {}

    public LayerSettingsDto(List<LayersDto> baseLayers, List<LayersDto> portLayers, List<LayersDto> additionalLayers, List<LayerAreaDto> areaLayers) {
        this.baseLayers = baseLayers;
        this.portLayers = portLayers;
        this.additionalLayers = additionalLayers;
        this.areaLayers = areaLayers;
    }

    @JsonProperty("areaLayers")
    public List<LayerAreaDto> getAreaLayers() {
        return areaLayers;
    }

    @JsonProperty("areaLayers")
    public void setAreaLayers(List<LayerAreaDto> areaLayers) {
        this.areaLayers = areaLayers;
    }

    @JsonProperty("additionalLayers")
    public List<LayersDto> getAdditionalLayers() {
        return additionalLayers;
    }

    @JsonProperty("additionalLayers")
    public void setAdditionalLayers(List<LayersDto> additionalLayers) {
        this.additionalLayers = additionalLayers;
    }

    @JsonProperty("baseLayers")
    public List<LayersDto> getBaseLayers() {
        return baseLayers;
    }

    @JsonProperty("baseLayers")
    public void setBaseLayers(List<LayersDto> baseLayers) {
        this.baseLayers = baseLayers;
    }

    @JsonProperty("portLayers")
    public List<LayersDto> getPortLayers() {
        return portLayers;
    }

    @JsonProperty("portLayers")
    public void setPortLayers(List<LayersDto> portLayers) {
        this.portLayers = portLayers;
    }
}
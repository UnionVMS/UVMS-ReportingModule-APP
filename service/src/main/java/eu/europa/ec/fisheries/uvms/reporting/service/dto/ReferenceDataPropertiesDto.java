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

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReferenceDataPropertiesDto implements Serializable{

    @JsonProperty("selection")
    private String selection;

    @JsonProperty("codes")
    private List<String> codes;

    public ReferenceDataPropertiesDto() {}

    public ReferenceDataPropertiesDto(String selection, List<String> codes) {
        this.selection = selection;
        this.codes = codes;
    }

    @JsonProperty("selection")
    public String getSelection() {
        return selection;
    }

    @JsonProperty("selection")
    public void setSelection(String selection) {
        this.selection = selection;
    }

    @JsonProperty("codes")
    public List<String> getCodes() {
        return codes;
    }

    @JsonProperty("codes")
    public void setCodes(List<String> codes) {
        this.codes = codes;
    }
}
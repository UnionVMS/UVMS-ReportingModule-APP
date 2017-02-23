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

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AlarmsDto implements Serializable{

    @JsonProperty("size")
    private Integer size;

    @JsonProperty("open")
    private String open;

    @JsonProperty("closed")
    private String closed;

    @JsonProperty("pending")
    private String pending;

    @JsonProperty("none")
    private String none;

    @JsonProperty("size")
    public Integer getSize() {
        return size;
    }

    @JsonProperty("size")
    public void setSize(Integer size) {
        this.size = size;
    }

    @JsonProperty("open")
    public String getOpen() {
        return open;
    }

    @JsonProperty("open")
    public void setOpen(String open) {
        this.open = open;
    }

    @JsonProperty("closed")
    public String getClosed() {
        return closed;
    }

    @JsonProperty("closed")
    public void setClosed(String closed) {
        this.closed = closed;
    }

    @JsonProperty("pending")
    public String getPending() {
        return pending;
    }

    @JsonProperty("pending")
    public void setPending(String pending) {
        this.pending = pending;
    }

    @JsonProperty("none")
    public String getNone() {
        return none;
    }

    @JsonProperty("none")
    public void setNone(String none) {
        this.none = none;
    }
}
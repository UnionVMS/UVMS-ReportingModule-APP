/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.dto.rules;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by padhyad on 3/25/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlarmMovement {

    @JsonProperty("id")
    private String movementId;

    @JsonProperty("x")
    private String xCoordinate;

    @JsonProperty("y")
    private String yCoordinate;

    @JsonProperty("id")
    public String getMovementId() {
        return movementId;
    }

    @JsonProperty("id")
    public void setMovementId(String movementId) {
        this.movementId = movementId;
    }

    @JsonProperty("x")
    public String getxCoordinate() {
        return xCoordinate;
    }

    @JsonProperty("x")
    public void setxCoordinate(String xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    @JsonProperty("y")
    public String getyCoordinate() {
        return yCoordinate;
    }

    @JsonProperty("y")
    public void setyCoordinate(String yCoordinate) {
        this.yCoordinate = yCoordinate;
    }
}
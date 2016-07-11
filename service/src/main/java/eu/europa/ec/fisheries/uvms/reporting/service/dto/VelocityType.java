/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import static javax.measure.unit.NonSI.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.measure.converter.UnitConverter;

public enum VelocityType {

    KPH(1, "kph"), MPH(2, "mph"), KTS(3, "kts");

    private Integer id;
    private String displayName;

    VelocityType(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonIgnore
    public UnitConverter getConverter() {

        UnitConverter unitConverter = null;

        switch (this){

            case KPH:
                unitConverter = KNOT.getConverterTo(KILOMETERS_PER_HOUR);
                break;

            case MPH:
                unitConverter = KNOT.getConverterTo(MILES_PER_HOUR);
                break;

            case KTS:
                unitConverter = KNOT.getConverterTo(KNOT);
                break;

            default:
                break;
        }

        return unitConverter;

    }
}
/*
 *
 * Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import lombok.EqualsAndHashCode;

/**
 * Created by padhyad on 11/30/2016.
 */
@EqualsAndHashCode(of = {"weightMin", "weightMax", "weightUnit"})
public class FaWeightDTO {

    private Double weightMin;

    private Double weightMax;

    private String weightUnit;

    public FaWeightDTO() {
    }

    public FaWeightDTO(Double weightMin, Double weightMax, String weightUnit) {
        this.weightMin = weightMin;
        this.weightMax = weightMax;
        this.weightUnit = weightUnit;
    }

    public Double getWeightMin() {
        return weightMin;
    }

    public void setWeightMin(Double weightMin) {
        this.weightMin = weightMin;
    }

    public Double getWeightMax() {
        return weightMax;
    }

    public void setWeightMax(Double weightMax) {
        this.weightMax = weightMax;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }
}

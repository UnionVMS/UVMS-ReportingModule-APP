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

package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.uvms.activity.model.schemas.SearchFilter;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.SingleValueTypeFilter;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by padhyad on 11/16/2016.
 */
@Embeddable
@EqualsAndHashCode
@ToString
public class FaWeight {

    @Column(name = "weight_min")
    private Double weightMin;

    @Column(name = "weight_max")
    private Double weightMax;

    @Column(name = "weight_unit")
    private String weightUnit;

    public FaWeight() {
    }

    public FaWeight(Double weightMin, Double weightMax, String weightUnit) {
        this.weightMin = weightMin;
        this.weightMax = weightMax;
        this.weightUnit = weightUnit;
    }

    public List<SingleValueTypeFilter> getFaFilters() {
        List<SingleValueTypeFilter> faFilterTypes = new ArrayList<>();
        if (weightMax != null) {
            faFilterTypes.add(new SingleValueTypeFilter(SearchFilter.QUANTITY_MAX, String.valueOf(weightMax)));
        }
        if (weightMin != null) {
            faFilterTypes.add(new SingleValueTypeFilter(SearchFilter.QUANTITY_MIN, String.valueOf(weightMin)));
        }
        if (weightUnit != null) {
            faFilterTypes.add(new SingleValueTypeFilter(SearchFilter.WEIGHT_MEASURE, String.valueOf(weightUnit)));
        }
        return faFilterTypes;
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

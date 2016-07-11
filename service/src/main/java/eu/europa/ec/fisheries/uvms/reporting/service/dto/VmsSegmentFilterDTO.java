/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europa.ec.fisheries.schema.movement.v1.SegmentCategoryType;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VmsSegmentFilterMapper;
import lombok.Builder;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false, of = {"minimumSpeed", "maximumSpeed", "maxDuration", "minDuration", "category"})
public class VmsSegmentFilterDTO extends FilterDTO {

    public static final String SEG_MIN_SPEED = "segMinSpeed";
    public static final String SEG_MAX_SPEED = "segMaxSpeed";
    public static final String SEG_MIN_DURATION = "segMinDuration";
    public static final String SEG_MAX_DURATION = "segMaxDuration";
    public static final String SEG_CATEGORY = "segCategory";
    public static final String VMS_SEGMENT = "vmssegment";

    @JsonProperty(SEG_MIN_SPEED)
    private Float minimumSpeed;

    @JsonProperty(SEG_MAX_SPEED)
    private Float maximumSpeed;

    @JsonProperty(SEG_MAX_DURATION)
    private Float maxDuration;

    @JsonProperty(SEG_MIN_DURATION)
    private Float minDuration;

    @JsonProperty(SEG_CATEGORY)
    private SegmentCategoryType category;

    public VmsSegmentFilterDTO() {
        super(FilterType.vmsseg);
    }

    public VmsSegmentFilterDTO(Long id, Long reportId) {
        super(FilterType.vmsseg, id, reportId);
    }

    @Builder
    public VmsSegmentFilterDTO(Long reportId,
                               Long id,
                               Float maxDuration,
                               Float minDuration,
                               Float minimumSpeed,
                               Float maximumSpeed,
                               SegmentCategoryType category) {
        this(id, reportId);
        this.minimumSpeed = minimumSpeed;
        this.maximumSpeed = maximumSpeed;
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
        this.category = category;
        validate();
    }

    public SegmentCategoryType getCategory() {
        return category;
    }

    public void setCategory(SegmentCategoryType category) {
        this.category = category;
    }

    @Override
    public Filter convertToFilter() {
        return VmsSegmentFilterMapper.INSTANCE.vmsSegmentFilterDTOToVmsSegmentFilter(this);
    }

    public Float getMinimumSpeed() {
        return minimumSpeed;
    }

    public void setMinimumSpeed(Float minimumSpeed) {
        this.minimumSpeed = minimumSpeed;
    }

    public Float getMaximumSpeed() {
        return maximumSpeed;
    }

    public void setMaximumSpeed(Float maximumSpeed) {
        this.maximumSpeed = maximumSpeed;
    }

    public Float getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(Float maxDuration) {
        this.maxDuration = maxDuration;
    }

    public Float getMinDuration() {
        return minDuration;
    }

    public void setMinDuration(Float minDuration) {
        this.minDuration = minDuration;
    }
}
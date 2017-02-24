/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.uvms.reporting.service.mapper.AreaFilterMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType.areas;

@Entity
@DiscriminatorValue("areas")
@EqualsAndHashCode(callSuper = true, of = {"areaType", "areaId"})
@ToString(callSuper = true)
public class AreaFilter extends Filter {

    @Column(name = "area_type")
    private String areaType;

    @Column(name = "area_id")
    private Long areaId;

    public AreaFilter() {
        super(areas);
    }

    @Builder
    public AreaFilter(Long areaId, String areaType) {
        super(areas);
        this.areaId = areaId;
        this.areaType = areaType;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    @Override
    public <T> T accept(FilterVisitor<T> visitor) {
        return visitor.visitAreaFilter(this);
    }

    @Override
    public void merge(Filter filter) {
        AreaFilterMapper.INSTANCE.merge((AreaFilter) filter, this);
    }

    @Override
    public Object getUniqKey() {
        return hashCode();
    }

    @Override
    public AreaIdentifierType getAreaIdentifierType() {
        return AreaFilterMapper.INSTANCE.areaIdentifierTypeToAreaFilter(this);
    }

}
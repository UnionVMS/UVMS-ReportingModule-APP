/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import static java.util.Arrays.asList;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeCriteria;
import eu.europa.ec.fisheries.schema.movement.v1.MovementActivityTypeType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.converter.ListStringConverter;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VmsPositionFilterMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

@Entity
@DiscriminatorValue("VMSPOS")
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class VmsPositionFilter extends Filter {

    @Column(name = "MIN_SPEED")
    private Float minimumSpeed;

    @Column(name = "MAX_SPEED")
    private Float maximumSpeed;

    @Column(name = "MOV_TYPE")
    private MovementTypeType movementType;

    @Column(name = "MOV_ACTIVITY")
    private MovementActivityTypeType movementActivity;

    @Convert(converter = ListStringConverter.class)
    @Column(name = "MOV_SOURCES")
    private List<String> movementSources;

    @Override
    public <T> T accept(FilterVisitor<T> visitor) {
        return visitor.visitVmsPositionFilter(this);
    }

    @Override
    public void merge(Filter filter) {
        VmsPositionFilterMapper.INSTANCE.merge((VmsPositionFilter) filter, this);
    }

    @Override
    public Object getUniqKey() {
        return getType();
    }

    @Override
    public List<ListCriteria> movementListCriteria() {
        List<ListCriteria> criteria = new ArrayList<>();
        if (movementActivity != null) {
            criteria.add(VmsPositionFilterMapper.INSTANCE.movementActivityToListCriteria(this));
        }
        if (movementType != null) {
            criteria.add(VmsPositionFilterMapper.INSTANCE.movementTypeToListCriteria(this));
        }
        if (movementSources != null) {
            for (String movementSource : movementSources) {
                criteria.add(VmsPositionFilterMapper.INSTANCE.movementSourceToListCriteria(movementSource));
            }
        }
        return criteria;
    }

    @Override
    public List<RangeCriteria> movementRangeCriteria(DateTime now) {
        return asList(VmsPositionFilterMapper.INSTANCE.speedRangeToRangeCriteria(this));
    }

}
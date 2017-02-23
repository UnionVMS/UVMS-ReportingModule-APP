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

import eu.europa.ec.fisheries.uvms.activity.model.schemas.ListValueTypeFilter;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.SearchFilter;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.SingleValueTypeFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.converter.ListStringConverter;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.FaFilterMapper;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("FAFILTER")
@EqualsAndHashCode(callSuper = true)
@ToString
public class FaFilter extends Filter {

    @Column(columnDefinition = "text", name = "report_types")
    @Convert(converter = ListStringConverter.class)
    private List<String> reportTypes;

    @Column(columnDefinition = "text", name = "activity_types")
    @Convert(converter = ListStringConverter.class)
    private List<String> activityTypes;

    @Column(columnDefinition = "text", name = "masters")
    @Convert(converter = ListStringConverter.class)
    private List<String> masters;

    @Column(columnDefinition = "text", name = "species")
    @Convert(converter = ListStringConverter.class)
    private List<String> species;

    @Column(columnDefinition = "text", name = "ports")
    @Convert(converter = ListStringConverter.class)
    private List<String> faPorts;

    @Column(columnDefinition = "text", name = "gears")
    @Convert(converter = ListStringConverter.class)
    private List<String> faGears;

    @Embedded
    private FaWeight faWeight;


    public FaFilter() {
        super(FilterType.fa);
    }

    @Builder
    public FaFilter(List<String> reportTypes, List<String> activityTypes, List<String> masters, List<String> faPorts, List<String> faGears, FaWeight faWeight, List<String> species) {
        super(FilterType.fa);
        this.reportTypes = reportTypes;
        this.activityTypes = activityTypes;
        this.masters = masters;
        this.faPorts = faPorts;
        this.faGears = faGears;
        this.faWeight = faWeight;
        this.species = species;
    }


    @Override
    public <T> T accept(FilterVisitor<T> visitor) {
        return visitor.visitFaFilter(this);
    }

    @Override
    public void merge(Filter filter) {
        FaFilterMapper.INSTANCE.merge((FaFilter) filter, this);
    }

    @Override
    public List<ListValueTypeFilter> getListValueFilters(DateTime now) {
        List<ListValueTypeFilter> faFilterTypes = new ArrayList<>();
        if (reportTypes != null && !reportTypes.isEmpty()) {
            faFilterTypes.add(new ListValueTypeFilter(SearchFilter.REPORT_TYPE, reportTypes));
        }
        if (activityTypes != null && !activityTypes.isEmpty()) {
            faFilterTypes.add(new ListValueTypeFilter(SearchFilter.ACTIVITY_TYPE, activityTypes));
        }

        if (masters != null && !masters.isEmpty()) {
            faFilterTypes.add(new ListValueTypeFilter(SearchFilter.MASTER, masters));
        }

        if (species != null && !species.isEmpty()) {
            faFilterTypes.add(new ListValueTypeFilter(SearchFilter.SPECIES, species));
        }

        if (faGears != null && !faGears.isEmpty()) {
            faFilterTypes.add(new ListValueTypeFilter(SearchFilter.GEAR, faGears));
        }

        if (faPorts != null && !faPorts.isEmpty()) {
            faFilterTypes.add(new ListValueTypeFilter(SearchFilter.PORT, faPorts));
        }
        return faFilterTypes;
    }

    public List<SingleValueTypeFilter> getSingleValueFilters(DateTime now) {
        List<SingleValueTypeFilter> faFilterTypes = new ArrayList<>();
        if (faWeight != null) {
            faFilterTypes.addAll(faWeight.getFaFilters());
        }
        return faFilterTypes;
    }

    @Override
    public Object getUniqKey() {
        return hashCode();
    }

    public List<String> getReportTypes() {
        return reportTypes;
    }

    public void setReportTypes(List<String> reportTypes) {
        this.reportTypes = reportTypes;
    }

    public List<String> getActivityTypes() {
        return activityTypes;
    }

    public void setActivityTypes(List<String> activityTypes) {
        this.activityTypes = activityTypes;
    }

    public List<String> getMasters() {
        return masters;
    }

    public void setMasters(List<String> masters) {
        this.masters = masters;
    }

    public List<String> getSpecies() {
        return species;
    }

    public void setSpecies(List<String> species) {
        this.species = species;
    }

    public List<String> getFaPorts() {
        return faPorts;
    }

    public void setFaPorts(List<String> faPorts) {
        this.faPorts = faPorts;
    }

    public List<String> getFaGears() {
        return faGears;
    }

    public void setFaGears(List<String> faGears) {
        this.faGears = faGears;
    }

    public FaWeight getFaWeight() {
        return faWeight;
    }

    public void setFaWeight(FaWeight faWeight) {
        this.faWeight = faWeight;
    }
}

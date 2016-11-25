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

import eu.europa.ec.fisheries.uvms.activity.model.schemas.FAFilterType;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.SearchFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.converter.ListStringConverter;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.FaFilterMapper;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by padhyad on 11/16/2016.
 */
@Entity
@DiscriminatorValue("FAFILTER")
@EqualsAndHashCode(callSuper = true)
@ToString
public class FaFilter extends Filter {

    @Column(name = "report_type")
    private String reportType;

    @Column(name = "activity_type")
    private String activityType;

    @Column(name = "master")
    private String master;

    @Column(columnDefinition = "text", name = "species")
    @Convert(converter = ListStringConverter.class)
    private List<String> species;

    @Embedded
    private FaPort faPort;

    @Embedded
    private FaGear faGear;

    @Embedded
    private FaWeight faWeight;


    public FaFilter() {
        super(FilterType.fa);
    }

    public FaFilter(String reportType, String activityType, String master, FaPort faPort, FaGear faGear, FaWeight faWeight, List<String> species) {
        super(FilterType.fa);
        this.reportType = reportType;
        this.activityType = activityType;
        this.master = master;
        this.faPort = faPort;
        this.faGear = faGear;
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
    public List<FAFilterType> getFaFilters(DateTime now) {
        List<FAFilterType> faFilterTypes = new ArrayList<>();
        faFilterTypes.add(new FAFilterType(SearchFilter.REPORT_TYPE, reportType));
        faFilterTypes.add(new FAFilterType(SearchFilter.ACTIVITY_TYPE, activityType));
        faFilterTypes.add(new FAFilterType(SearchFilter.MASTER, master));
        if (CollectionUtils.isEmpty(species)) {
            faFilterTypes.add(new FAFilterType(SearchFilter.SPECIES, species.get(0)));
        }
        if (faWeight != null) {
            faFilterTypes.addAll(faWeight.getFaFilters());
        }
        return faFilterTypes;
    }

    @Override
    public Object getUniqKey() {
        return getId();
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public FaPort getFaPort() {
        return faPort;
    }

    public void setFaPort(FaPort faPort) {
        this.faPort = faPort;
    }

    public FaGear getFaGear() {
        return faGear;
    }

    public void setFaGear(FaGear faGear) {
        this.faGear = faGear;
    }

    public FaWeight getFaWeight() {
        return faWeight;
    }

    public void setFaWeight(FaWeight faWeight) {
        this.faWeight = faWeight;
    }

    public List<String> getSpecies() {
        return species;
    }

    public void setSpecies(List<String> species) {
        this.species = species;
    }
}

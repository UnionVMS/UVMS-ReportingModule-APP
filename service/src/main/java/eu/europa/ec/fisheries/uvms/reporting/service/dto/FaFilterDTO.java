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

import java.util.List;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.FaFilterMapper;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true, of = {"reportTypes", "activityTypes", "masters", "faPorts", "faGears", "species", "faWeight"})
@ToString
public class FaFilterDTO extends FilterDTO {

    private List<String> reportTypes;

    private List<String> activityTypes;

    private List<String> masters;

    private List<String> faPorts;

    private List<String> faGears;

    private List<String> species;

    private FaWeight faWeight;

    public FaFilterDTO() {
        super(FilterType.fa);
    }

    public FaFilterDTO(Long id, Long reportId) {
        super(FilterType.fa, id, reportId);
    }

    @Builder(builderMethodName = "FaFilterBuilder")
    public FaFilterDTO(Long reportId, Long id, List<String> reportTypes, List<String> activityTypes,
                       List<String> masters, List<String> ports, List<String> gears, List<String> species,
                       FaWeight faWeight) {
        this(id, reportId);
        this.reportTypes = reportTypes;
        this.activityTypes = activityTypes;
        this.masters = masters;
        this.faPorts = ports;
        this.faGears = gears;
        this.species = species;
        this.faWeight = faWeight;
        validate();
    }

    @Override
    public Filter convertToFilter() {
        return FaFilterMapper.INSTANCE.faFilterDtoToFaFilter(this);
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

    public List<String> getSpecies() {
        return species;
    }

    public void setSpecies(List<String> species) {
        this.species = species;
    }

    public FaWeight getFaWeight() {
        return faWeight;
    }

    public void setFaWeight(FaWeight faWeight) {
        this.faWeight = faWeight;
    }
}

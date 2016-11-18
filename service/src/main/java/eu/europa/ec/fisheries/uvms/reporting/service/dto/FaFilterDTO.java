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

import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.FaFilterMapper;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by padhyad on 11/16/2016.
 */
@EqualsAndHashCode(callSuper = true)
public class FaFilterDTO extends FilterDTO {

    private String reportType;

    private String activityType;

    private String master;

    private String departurePort;

    private String arrivalPort;

    private String landingPort;

    private String gearOnboard;

    private String gearDeployed;

    private List<String> species;

    private Double weightMin;

    private Double weightMax;

    private String weightUnit;

    public FaFilterDTO() {
        super(FilterType.fa);
    }

    public FaFilterDTO(Long id, Long reportId) {
        super(FilterType.fa, id, reportId);
    }

    @Builder(builderMethodName = "FaFilterBuilder")
    public FaFilterDTO(Long reportId, Long id, String reportType, String activityType, String master, String departurePort, String arrivalPort, String landingPort, String gearOnboard, String gearDeployed, List<String> species, Double weightMin, Double weightMax, String weightUnit) {
        this(id, reportId);
        this.reportType = reportType;
        this.activityType = activityType;
        this.master = master;
        this.departurePort = departurePort;
        this.arrivalPort = arrivalPort;
        this.landingPort = landingPort;
        this.gearOnboard = gearOnboard;
        this.gearDeployed = gearDeployed;
        this.species = species;
        this.weightMin = weightMin;
        this.weightMax = weightMax;
        this.weightUnit = weightUnit;
        validate();
    }

    @Override
    public Filter convertToFilter() {
        return FaFilterMapper.INSTANCE.faFilterDtoToFaFilter(this);
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

    public String getDeparturePort() {
        return departurePort;
    }

    public void setDeparturePort(String departurePort) {
        this.departurePort = departurePort;
    }

    public String getArrivalPort() {
        return arrivalPort;
    }

    public void setArrivalPort(String arrivalPort) {
        this.arrivalPort = arrivalPort;
    }

    public String getLandingPort() {
        return landingPort;
    }

    public void setLandingPort(String landingPort) {
        this.landingPort = landingPort;
    }

    public String getGearOnboard() {
        return gearOnboard;
    }

    public void setGearOnboard(String gearOnboard) {
        this.gearOnboard = gearOnboard;
    }

    public String getGearDeployed() {
        return gearDeployed;
    }

    public void setGearDeployed(String gearDeployed) {
        this.gearDeployed = gearDeployed;
    }

    public List<String> getSpecies() {
        return species;
    }

    public void setSpecies(List<String> species) {
        this.species = species;
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

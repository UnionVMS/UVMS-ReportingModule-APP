/*
 *
 * Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.
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

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class FaFilter {

    @JsonProperty("reportTypes")
    private List<String> reportTypes;

    @JsonProperty("activityTypes")
    private List<String> activityTypes;

    @JsonProperty("masters")
    private List<String> masters;

    @JsonProperty("ports")
    private List<String> faPorts;

    @JsonProperty("gears")
    private List<String> faGears;

    @JsonProperty("species")
    private List<String> species;

    @JsonProperty("weights")
    private FaWeight faWeight;

    @JsonProperty("reportTypes")
    public List<String> getReportTypes() {
        return reportTypes;
    }

    @JsonProperty("reportTypes")
    public void setReportTypes(List<String> reportTypes) {
        this.reportTypes = reportTypes;
    }

    @JsonProperty("activityTypes")
    public List<String> getActivityTypes() {
        return activityTypes;
    }

    @JsonProperty("activityTypes")
    public void setActivityTypes(List<String> activityTypes) {
        this.activityTypes = activityTypes;
    }

    @JsonProperty("masters")
    public List<String> getMasters() {
        return masters;
    }

    @JsonProperty("masters")
    public void setMasters(List<String> masters) {
        this.masters = masters;
    }

    @JsonProperty("ports")
    public List<String> getFaPorts() {
        return faPorts;
    }

    @JsonProperty("ports")
    public void setFaPorts(List<String> faPorts) {
        this.faPorts = faPorts;
    }

    @JsonProperty("gears")
    public List<String> getFaGears() {
        return faGears;
    }

    @JsonProperty("gears")
    public void setFaGears(List<String> faGears) {
        this.faGears = faGears;
    }

    public List<String> getSpecies() {
        return species;
    }

    public void setSpecies(List<String> species) {
        this.species = species;
    }

    @JsonProperty("weights")
    public FaWeight getFaWeight() {
        return faWeight;
    }

    @JsonProperty("weights")
    public void setFaWeight(FaWeight faWeight) {
        this.faWeight = faWeight;
    }
}

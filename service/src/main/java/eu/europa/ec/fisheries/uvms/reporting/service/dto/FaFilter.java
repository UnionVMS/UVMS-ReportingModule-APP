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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
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

    @JsonProperty("weight")
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

    @JsonProperty("species")
    public List<String> getSpecies() {
        return species;
    }

    @JsonProperty("species")
    public void setSpecies(List<String> species) {
        this.species = species;
    }

    @JsonProperty("weight")
    public FaWeight getFaWeight() {
        return faWeight;
    }

    @JsonProperty("weight")
    public void setFaWeight(FaWeight faWeight) {
        this.faWeight = faWeight;
    }
}

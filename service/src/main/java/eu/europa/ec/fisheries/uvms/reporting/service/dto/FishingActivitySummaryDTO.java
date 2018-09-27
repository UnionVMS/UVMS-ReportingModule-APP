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

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;

import eu.europa.ec.fisheries.uvms.activity.model.schemas.VesselIdentifierType;

public class FishingActivitySummaryDTO {

    private int activityId;

    private int faReportID;

    private String activityType;

    private String geometry;

    private XMLGregorianCalendar acceptedDateTime;

    private String dataSource;

    private String reportType;

    private String purposeCode;

    private String vesselName;

    private String vesselGuid;

    private String tripId;

    private String flagState;
    private boolean isCorrection;
    private List<String> gears;
    private List<String> species;
    private List<String> ports;
    private List<String> areas;
    private List<VesselIdentifierType> vesselIdentifiers;

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public XMLGregorianCalendar getAcceptedDateTime() {
        return acceptedDateTime;
    }

    public void setAcceptedDateTime(XMLGregorianCalendar acceptedDateTime) {
        this.acceptedDateTime = acceptedDateTime;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getPurposeCode() {
        return purposeCode;
    }

    public void setPurposeCode(String purposeCode) {
        this.purposeCode = purposeCode;
    }

    public String getVesselName() {
        return vesselName;
    }

    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    public String getVesselGuid() {
        return vesselGuid;
    }

    public void setVesselGuid(String vesselGuid) {
        this.vesselGuid = vesselGuid;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getFlagState() {
        return flagState;
    }

    public void setFlagState(String flagState) {
        this.flagState = flagState;
    }

    public boolean isCorrection() {
        return isCorrection;
    }

    public void setCorrection(boolean correction) {
        isCorrection = correction;
    }

    public List<String> getSpecies() {
        return species;
    }

    public void setSpecies(List<String> species) {
        this.species = species;
    }

    public List<String> getGears() {
        return gears;
    }

    public void setGears(List<String> gears) {
        this.gears = gears;
    }

    public List<String> getPorts() {
        return ports;
    }

    public void setPorts(List<String> ports) {
        this.ports = ports;
    }

    public List<String> getAreas() {
        return areas;
    }

    public void setAreas(List<String> areas) {
        this.areas = areas;
    }

    public List<VesselIdentifierType> getVesselIdentifiers() {
        return vesselIdentifiers;
    }

    public void setVesselIdentifiers(List<VesselIdentifierType> vesselIdentifiers) {
        this.vesselIdentifiers = vesselIdentifiers;
    }

    public int getFaReportID() {
        return faReportID;
    }

    public void setFaReportID(int faReportID) {
        this.faReportID = faReportID;
    }
}

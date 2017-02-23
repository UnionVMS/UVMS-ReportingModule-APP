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

package eu.europa.ec.fisheries.uvms.reporting.dto;

/**
 * Created by padhyad on 12/6/2016.
 */
public class TripDTO {

    private String tripId;

    private String schemeId;

    private String multipointWkt;

    public TripDTO() {
    }

    public TripDTO(String tripId, String schemeId, String multipointWkt) {
        this.tripId = tripId;
        this.schemeId = schemeId;
        this.multipointWkt = multipointWkt;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(String schemeId) {
        this.schemeId = schemeId;
    }

    public String getMultipointWkt() {
        return multipointWkt;
    }

    public void setMultipointWkt(String multipointWkt) {
        this.multipointWkt = multipointWkt;
    }
}

/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import java.util.Collection;
import java.util.Map;

import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;

public class MovementData {

    Collection<MovementMapResponseType> movementMap;
    Map<String, MovementMapResponseType> responseTypeMap;
    Map<String, eu.europa.ec.fisheries.wsdl.asset.types.Asset> assetMap;

    public Map<String, MovementMapResponseType> getResponseTypeMap() {
        return responseTypeMap;
    }

    public void setResponseTypeMap(Map<String, MovementMapResponseType> responseTypeMap) {
        this.responseTypeMap = responseTypeMap;
    }

    public Map<String, eu.europa.ec.fisheries.wsdl.asset.types.Asset> getAssetMap() {
        return assetMap;
    }

    public void setAssetMap(Map<String, eu.europa.ec.fisheries.wsdl.asset.types.Asset> assetMap) {
        this.assetMap = assetMap;
    }

    public Collection<MovementMapResponseType> getMovementMap() {
        return movementMap;
    }

    public void setMovementMap(Collection<MovementMapResponseType> movementMap) {
        this.movementMap = movementMap;
    }

}

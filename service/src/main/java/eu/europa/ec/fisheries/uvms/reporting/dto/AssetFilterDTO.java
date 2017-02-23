/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.dto;

import eu.europa.ec.fisheries.uvms.reporting.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.mapper.AssetFilterMapper;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true, of = {"guid", "name"})
public class AssetFilterDTO extends FilterDTO {

    private static final int MAX_SIZE = 255;
    private static final int MIN_SIZE = 1;

    public static final String NAME = "name";
    public static final String GUID = "guid";
    public static final String ASSETS = "assets";

    @Size(min = MIN_SIZE, max = MAX_SIZE)
    @NotNull
    private String guid;

    @Size(min = MIN_SIZE, max = MAX_SIZE)
    @NotNull
    private String name;

    public AssetFilterDTO() {
        super(FilterType.asset);
    }

    public AssetFilterDTO(Long id, Long reportId) {
        super(FilterType.asset, id, reportId);
    }

    @Builder(builderMethodName = "AssetFilterDTOBuilder")
    public AssetFilterDTO(Long id, Long reportId, String guid, String name) {
        this(id, reportId);
        this.guid = guid;
        this.name = name;
        validate();
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Filter convertToFilter() {
        return AssetFilterMapper.INSTANCE.assetFilterDTOToAssetFilter(this);
    }
}
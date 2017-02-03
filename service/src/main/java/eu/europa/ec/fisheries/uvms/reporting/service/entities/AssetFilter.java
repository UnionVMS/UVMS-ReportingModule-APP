/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.AssetFilterMapper;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListCriteriaPair;

import java.util.Arrays;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType.asset;
import static java.util.Arrays.asList;

@Entity
@DiscriminatorValue("ASSET")
@EqualsAndHashCode(callSuper = false, of = {"guid"})
@ToString(callSuper = true)
public class AssetFilter extends Filter {

    @NotNull
    private String guid;

    @NotNull
    private String name;

    public AssetFilter() {
        super(asset);
    }

    @Builder
    public AssetFilter(String guid, String name) {
        super(asset);
        this.guid = guid;
        this.name = name;
    }

    @Override
    public <T> T accept(FilterVisitor<T> visitor) {
        return visitor.visitAssetFilter(this);
    }

    @Override
    public void merge(Filter filter) {
        AssetFilterMapper.INSTANCE.merge((AssetFilter) filter, this);
    }

    @Override
    public Object getUniqKey() {
        return getGuid();
    }

    @Override
    public List<AssetListCriteriaPair> assetCriteria() {
        return asList(AssetFilterMapper.INSTANCE.assetFilterToAssetListCriteriaPair(this));
    }

    @Override
    public List<ListCriteria> movementListCriteria() {
        return asList(AssetFilterMapper.INSTANCE.assetFilterToListCriteria(this));
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

}
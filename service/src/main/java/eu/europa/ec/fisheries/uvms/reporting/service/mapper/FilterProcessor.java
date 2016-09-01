/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementQuery;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.SearchKey;
import eu.europa.ec.fisheries.uvms.exception.ProcessorException;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.AreaFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import eu.europa.ec.fisheries.wsdl.asset.group.AssetGroup;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListCriteria;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListCriteriaPair;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListPagination;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListQuery;
import org.joda.time.DateTime;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

public class FilterProcessor {

    private final Set<ListCriteria> movementListCriteria = new HashSet<>();
    private final Set<RangeCriteria> rangeCriteria = new HashSet<>();
    private final Set<AssetListCriteriaPair> assetListCriteriaPairs = new HashSet<>();
    private final Set<AssetGroup> assetGroupList = new HashSet<>();
    private final Set<AreaIdentifierType> areaIdentifierList = new HashSet<>();
    private DateTime now;

    public FilterProcessor(Set<Filter> filters, DateTime now) throws ProcessorException {
        this.now = now;
        if (isEmpty(filters)) {
            throw new ProcessorException("");
        }

        for (Filter filter : filters) {
            if(filter != null){
                addCriteria(filter);
                addAreaIdentifier(filter);
            }
        }
    }

    public void addAreaCriteria(String areaWkt) {
        if (areaWkt != null) {
            ListCriteria areaCriteria = new ListCriteria();
            areaCriteria.setKey(SearchKey.AREA);
            areaCriteria.setValue(areaWkt);
            movementListCriteria.add(areaCriteria);
        }
    }

    private void addCriteria(Filter filter) {
        assetListCriteriaPairs.addAll(filter.assetCriteria());
        assetGroupList.addAll(filter.assetGroupCriteria());
        rangeCriteria.addAll(filter.movementRangeCriteria(now));
        movementListCriteria.addAll(filter.movementListCriteria());
    }

    private void addAreaIdentifier(Filter filter) {
        if (filter instanceof AreaFilter) { // TODO instanceof not really needed to be checked
            areaIdentifierList.add(filter.getAreaIdentifierType());
        }
    }

    public MovementQuery toMovementQuery() {
        MovementQuery movementQuery = new MovementQuery();
        movementQuery.getMovementSearchCriteria().addAll(movementListCriteria);
        movementQuery.getMovementRangeSearchCriteria().addAll(rangeCriteria);
        movementQuery.setExcludeFirstAndLastSegment(true);
        return movementQuery;
    }

    public AssetListQuery toAssetListQuery() {
        AssetListQuery query = new AssetListQuery();

        if (isNotEmpty(assetListCriteriaPairs)) {
            query.setAssetSearchCriteria(createListCriteria());
            AssetListPagination pagination = new AssetListPagination();
            pagination.setPage(1);
            pagination.setListSize(1000);
            query.setPagination(pagination);
        }

        return query;
    }

    private AssetListCriteria createListCriteria() {
        AssetListCriteria assetListCriteria = new AssetListCriteria();
        assetListCriteria.setIsDynamic(false);
        assetListCriteria.getCriterias().addAll(assetListCriteriaPairs);
        return assetListCriteria;
    }

    public Set<AssetGroup> getAssetGroupList() {
        return assetGroupList;
    }

    public boolean hasAssetsOrAssetGroups() {
        return hasAssets() || hasAssetGroups();
    }

    public boolean hasAssets() {
        return !assetListCriteriaPairs.isEmpty();
    }

    public boolean hasAssetGroups() {
        return !assetGroupList.isEmpty();
    }

    public Set<ListCriteria> getMovementListCriteria() {
        return movementListCriteria;
    }

    public Set<AssetListCriteriaPair> getAssetListCriteriaPairs() {
        return assetListCriteriaPairs;
    }

    public Set<RangeCriteria> getRangeCriteria() {
        return rangeCriteria;
    }

    public Set<AreaIdentifierType> getAreaIdentifierList() {
        return areaIdentifierList;
    }

    public DateTime getNow() {

        return now;
    }

    public void setNow(DateTime now) {

        this.now = now;
    }
}
package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.schema.movement.search.v1.*;
import eu.europa.ec.fisheries.uvms.exception.ProcessorException;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.AreaFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import eu.europa.ec.fisheries.wsdl.asset.group.AssetGroup;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListCriteria;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListCriteriaPair;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListPagination;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListQuery;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * This class is responsible reading the report filters and transform them
 * to search queries for asset and movement queues
 */
public class FilterProcessor {

    private final Set<ListCriteria> movementListCriteria = new HashSet<>();
    private final Set<RangeCriteria> rangeCriteria = new HashSet<>();
    private final Set<AssetListCriteriaPair> assetListCriteriaPairs = new HashSet<>();
    private final Set<AssetGroup> assetGroupList = new HashSet<>();
    private final Set<AreaIdentifierType> areaIdentifierList = new HashSet<AreaIdentifierType>();

    public FilterProcessor(Set<Filter> filters) throws ProcessorException {
        validate(filters);

        for (Filter filter : filters) {
            addCriteria(filter);
            addAreaIdentifier(filter);
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
        rangeCriteria.addAll(filter.movementRangeCriteria());
        movementListCriteria.addAll(filter.movementListCriteria());
    }
    
    private void addAreaIdentifier(Filter filter) {
    	if (filter instanceof AreaFilter) {
    		areaIdentifierList.add(filter.getAreaIdentifierType());
    	}
    }

    private void validate(Set<Filter> filters) throws ProcessorException {
        if (isEmpty(filters)) {
            throw new ProcessorException("");
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
            query.setPagination(createPagination());
        }

        return query;
    }

    private AssetListPagination createPagination() {
        AssetListPagination pagination = new AssetListPagination();
        pagination.setPage(1);
        pagination.setListSize(1000);
        return pagination;
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
        return hasAssets() | hasAssetGroups();
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
}

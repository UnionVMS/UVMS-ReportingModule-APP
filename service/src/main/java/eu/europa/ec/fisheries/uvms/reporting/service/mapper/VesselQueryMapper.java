package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselFilter;
import eu.europa.ec.fisheries.wsdl.vessel.types.ConfigSearchField;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListCriteria;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListCriteriaPair;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListPagination;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListQuery;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * //TODO create test
 */
public class VesselQueryMapper {

    private List<VesselListCriteriaPair> vesselListCriteriaPairs;

    public VesselQueryMapper(){
        vesselListCriteriaPairs = new ArrayList<>();
    }

    public void addCriteria(final Filter next){
        VesselFilter filter = (VesselFilter) next;
        VesselListCriteriaPair criteriaPair = new VesselListCriteriaPair();
        criteriaPair.setKey(ConfigSearchField.GUID);
        criteriaPair.setValue(filter.getGuid());
        vesselListCriteriaPairs.add(criteriaPair);
    }

    public VesselListQuery getQuery() {

        VesselListQuery query = null;

        if (vesselListCriteriaPairs != null && vesselListCriteriaPairs.size()>0){
            query = new VesselListQuery();
            VesselListCriteria vesselListCriteria = new VesselListCriteria();
            vesselListCriteria.setIsDynamic(false);
            vesselListCriteria.getCriterias().addAll(vesselListCriteriaPairs);
            query.setVesselSearchCriteria(vesselListCriteria);
            VesselListPagination pagination = new VesselListPagination();
            pagination.setPage(BigInteger.valueOf(1));
            pagination.setListSize(new BigInteger("1000"));
            query.setPagination(pagination);
        }

        return query;
    }
}

package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VesselGroupFilter;
import eu.europa.ec.fisheries.wsdl.vessel.group.VesselGroup;
import eu.europa.ec.fisheries.wsdl.vessel.group.VesselGroupSearchField;
import eu.europa.ec.fisheries.wsdl.vessel.types.ConfigSearchField;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListCriteriaPair;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * //TODO create test
 */
public class VesselGroupQueryMapper {

    private List<VesselGroup> vesselGroupList;

    public VesselGroupQueryMapper() {
        vesselGroupList = new ArrayList<>();
    }

    public void addCriteria(final Filter next) {
        VesselGroup vesselGroup = new VesselGroup();
        VesselGroupFilter filter = (VesselGroupFilter) next;
        vesselGroup.setId(new BigInteger(filter.getGroupId()));
        vesselGroup.setDynamic(false);
        vesselGroupList.add(vesselGroup);
    }

    public void getQuery() {

//        vesselGroup.setDynamic(false);
//        vesselGroup.getSearchFields().addAll(vesselGroupSearchFields);
//        vesselGroup.se
    }
}

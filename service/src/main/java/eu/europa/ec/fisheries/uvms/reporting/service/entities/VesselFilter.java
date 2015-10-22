package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.SearchKey;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VesselFilterMapper;
import eu.europa.ec.fisheries.wsdl.vessel.types.ConfigSearchField;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListCriteriaPair;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@DiscriminatorValue("VESSEL")
@EqualsAndHashCode(callSuper = true, of = {"guid", "name"})
public class VesselFilter extends Filter {

    @NotNull
    private String guid;

    @NotNull
    private String name;

    VesselFilter() {
        super(FilterType.vessel);
    }

    @Builder(builderMethodName = "VesselFilterBuilder")
    public VesselFilter(Long id, String guid, String name) {
        super(FilterType.vessel);
        this.guid = guid;
        this.name = name;
        setId(id);
    }

    @Override
    public FilterDTO convertToDTO() {
        return VesselFilterMapper.INSTANCE.vesselFilterToVesselFilterDTO(this);
    }

    @Override
    public void merge(Filter filter) {
        VesselFilter incoming = (VesselFilter) filter;
        setGuid(incoming.getGuid());
        setName(incoming.getName());
    }

    @Override
    public Object getUniqKey() {
        return hashCode();
    }

    @Override
    public List<VesselListCriteriaPair> vesselCriteria() {
        VesselListCriteriaPair criteriaPair = new VesselListCriteriaPair();
        criteriaPair.setKey(ConfigSearchField.GUID);
        criteriaPair.setValue(guid);
        return Lists.newArrayList(criteriaPair);
    }

    @Override
    public List<ListCriteria> movementCriteria() {
        ListCriteria listCriteria = new ListCriteria();
        listCriteria.setKey(SearchKey.CONNECT_ID);
        listCriteria.setValue(getGuid());
        return Lists.newArrayList(listCriteria);
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

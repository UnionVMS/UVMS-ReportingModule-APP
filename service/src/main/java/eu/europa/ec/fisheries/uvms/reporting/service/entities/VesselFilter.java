package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VesselFilterMapper;
import eu.europa.ec.fisheries.wsdl.vessel.types.ConfigSearchField;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListCriteriaPair;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("VESSEL")
@EqualsAndHashCode(callSuper = true)
public class VesselFilter extends Filter {

    private String guid;

    private String name;

    public VesselFilter() {
        super(FilterType.VESSEL);
    }

    @Override
    public FilterDTO convertToDTO() {
        return VesselFilterMapper.INSTANCE.vesselFilterToVesselFilterDTO(this);
    }

    @Override
    public void merge(Filter filter) {
        VesselFilter incoming = (VesselFilter) filter;
        this.setGuid(incoming.getGuid());
        this.setName(incoming.getName());
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

    public VesselListCriteriaPair vesselListCriteriaPair(){
        VesselListCriteriaPair criteriaPair = new VesselListCriteriaPair();
        criteriaPair.setKey(ConfigSearchField.GUID);
        criteriaPair.setValue(guid);
        return criteriaPair;
    }

}

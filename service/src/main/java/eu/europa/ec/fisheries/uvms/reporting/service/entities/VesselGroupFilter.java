package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VesselGroupFilterMapper;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("VGROUP")
@EqualsAndHashCode(callSuper = true)
public class VesselGroupFilter extends Filter {

    private String guid;

    private String userName;

    public VesselGroupFilter() {
        super(FilterType.vgroup);
    }

    @Builder(builderMethodName = "VesselGroupFilterBuilder")
    public VesselGroupFilter(String groupId, String userName){
        super(FilterType.vgroup);
        this.guid = groupId;
        this.userName = userName;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public FilterDTO convertToDTO() {
        return VesselGroupFilterMapper.INSTANCE.vesselGroupFilterToVesselGroupFilterDTO(this);
    }

    @Override
    public void merge(Filter filter) {
        VesselGroupFilter incoming = (VesselGroupFilter) filter;
        this.setUserName(incoming.getUserName());
        this.setGuid(incoming.getGuid());
    }
}

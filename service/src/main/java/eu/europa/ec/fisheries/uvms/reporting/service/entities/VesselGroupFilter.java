package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VesselGroupFilterMapper;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("VGROUP")
@EqualsAndHashCode(callSuper = true)
public class VesselGroupFilter extends Filter {

    private String groupId;

    private String userName;

    public VesselGroupFilter() {
        super(FilterType.VGROUP);
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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
        this.setGroupId(incoming.getGroupId());
    }
}

package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VesselGroupFilterMapper;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@DiscriminatorValue("VGROUP")
@EqualsAndHashCode(callSuper = true, of = {"guid", "name", "userName"})
public class VesselGroupFilter extends Filter {

    @NotNull
    private String guid;

    @NotNull
    private String name;

    @NotNull
    private String userName;

    public VesselGroupFilter() {
        super(FilterType.vgroup);
    }

    @Builder(builderMethodName = "VesselGroupFilterBuilder")
    public VesselGroupFilter(Long id, String groupId, String userName, String name){
        super(FilterType.vgroup);
        this.guid = groupId;
        this.userName = userName;
        this.name = name;
        setId(id);
    }

    @Override
    public FilterDTO convertToDTO() {
        return VesselGroupFilterMapper.INSTANCE.vesselGroupFilterToVesselGroupFilterDTO(this);
    }

    @Override
    public void merge(Filter filter) {
        VesselGroupFilter incoming = (VesselGroupFilter) filter;
        setUserName(incoming.getUserName());
        setGuid(incoming.getGuid());
        setName(incoming.getName());
    }

    @Override
    public Object getUniqKey() {
        return hashCode();
    } //FIXME better to use GUID as unique key but test first

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

}

package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.uvms.reporting.service.mapper.FilterVisitor;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VesselGroupFilterMapper;
import eu.europa.ec.fisheries.wsdl.vessel.group.VesselGroup;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

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

    @Builder
    public VesselGroupFilter(Long id, String groupId, String userName, String name){
        super(FilterType.vgroup);
        this.guid = groupId;
        this.userName = userName;
        this.name = name;
        setId(id);
    }

    @Override
    public <T> T accept(FilterVisitor<T> visitor) {
        return visitor.visitVesselGroupFilter(this);
    }

    @Override
    public void merge(Filter filter) {
        VesselGroupFilterMapper.INSTANCE.merge((VesselGroupFilter) filter, this);
    }

    @Override
    public List<VesselGroup> vesselGroupCriteria(){
        return Arrays.asList(VesselGroupFilterMapper.INSTANCE.vesselGroupFilterToVesselGroup(this));
    }

    @Override
    public Object getUniqKey() {
        return hashCode();
    }

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

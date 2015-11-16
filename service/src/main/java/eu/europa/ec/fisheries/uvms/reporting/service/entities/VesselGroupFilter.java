package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.FilterVisitor;
import eu.europa.ec.fisheries.wsdl.vessel.group.VesselGroup;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
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

    @Builder(builderMethodName = "VesselGroupFilterBuilder")
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
        VesselGroupFilter incoming = (VesselGroupFilter) filter;
        setUserName(incoming.getUserName());
        setGuid(incoming.getGuid());
        setName(incoming.getName());
    }

    @Override
    public List<VesselGroup> vesselGroupCriteria(){
        VesselGroup vesselGroup = new VesselGroup();
        if (StringUtils.isNotBlank(getGuid())) {
            vesselGroup.setGuid(getGuid());
            vesselGroup.setName(getName());
            vesselGroup.setUser(getUserName());
            vesselGroup.setDynamic(true);
        }
        return Lists.newArrayList(vesselGroup);
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

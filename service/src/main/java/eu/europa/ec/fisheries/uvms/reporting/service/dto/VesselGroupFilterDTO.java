package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import eu.europa.ec.fisheries.uvms.reporting.service.visitor.FilterDTOVisitor;

@JsonTypeName("vgroup")
public class VesselGroupFilterDTO extends FilterDTO {

    private String guid;
    private String groupId;
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public <T> T accept(FilterDTOVisitor<T> visitor) {
        return visitor.visitVesselGroupFilterDTO(this);
    }
}

package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VesselGroupFilterMapper;
import lombok.Builder;

@JsonTypeName("vgroup")
public class VesselGroupFilterDTO extends FilterDTO {

    public static final String GROUP_ID = "groupId" ;
    public static final String USER = "user" ;

    private String groupId;

    @JsonProperty(USER)
    private String userName;

    @Builder(builderMethodName = "VesselGroupFilterDTOBuilder")
    public VesselGroupFilterDTO(Long reportId, Long id,
                                String groupId,
                                String userName) {
        this.groupId = groupId;
        this.userName = userName;
        setId(id);
        setReportId(reportId);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public Filter convertToFilter() {
        return VesselGroupFilterMapper.INSTANCE.vesselGroupFilterDTOToVesselGroupFilter(this);
    }
}

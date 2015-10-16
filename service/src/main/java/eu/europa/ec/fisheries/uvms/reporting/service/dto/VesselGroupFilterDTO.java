package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VesselGroupFilterMapper;
import lombok.Builder;
import org.apache.commons.lang3.NotImplementedException;

public class VesselGroupFilterDTO extends FilterDTO {

    public static final String GUID = "guid" ;
    public static final String USER = "user" ;
    public static final String NAME = "name" ;

    private String guid;

    @JsonProperty(USER)
    private String userName;

    private String name;

    @Builder(builderMethodName = "VesselGroupFilterDTOBuilder")
    public VesselGroupFilterDTO(Long reportId, Long id,
                                String guid,
                                String userName,
                                String name) {
        this.guid = guid;
        this.userName = userName;
        this.name = name;
        setId(id);
        setReportId(reportId);
        setType(FilterType.vgroup);
    }

    @Override
    public void validate() {
        throw new NotImplementedException("TODO"); // TODO
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String groupId) {
        this.guid = groupId;
    }

    @Override
    public Filter convertToFilter() {
        return VesselGroupFilterMapper.INSTANCE.vesselGroupFilterDTOToVesselGroupFilter(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

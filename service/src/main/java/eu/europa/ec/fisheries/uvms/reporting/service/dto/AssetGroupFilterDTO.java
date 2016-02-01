package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.AssetGroupFilterMapper;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true, of = {"guid", "name", "userName"})
public class AssetGroupFilterDTO extends FilterDTO {

    public static final String GUID = "guid";
    public static final String USER = "user";
    public static final String NAME = "name";

    @NotNull
    private String guid;

    @JsonProperty(USER)
    @NotNull
    private String userName;

    @NotNull
    private String name;

    public AssetGroupFilterDTO() {
        super(FilterType.vgroup);
    }

    public AssetGroupFilterDTO(Long id, Long reportId) {
        super(FilterType.vgroup, id, reportId);
    }

    @Builder(builderMethodName = "AssetGroupFilterDTOBuilder")
    public AssetGroupFilterDTO(Long reportId, Long id,
                                String guid,
                                String userName,
                                String name) {
        this(id, reportId);
        this.guid = guid;
        this.userName = userName;
        this.name = name;
        validate();
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
        return AssetGroupFilterMapper.INSTANCE.assetGroupFilterDTOToAssetGroupFilter(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

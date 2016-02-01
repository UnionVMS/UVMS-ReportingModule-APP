package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.AssetFilterMapper;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true, of = {"guid", "name"})
public class AssetFilterDTO extends FilterDTO {

    private static final int MAX_SIZE = 255;
    private static final int MIN_SIZE = 1;

    public static final String NAME = "name";
    public static final String GUID = "guid";
    public static final String ASSETS = "assets";

    @Size(min = MIN_SIZE, max = MAX_SIZE)
    @NotNull
    private String guid;

    @Size(min = MIN_SIZE, max = MAX_SIZE)
    @NotNull
    private String name;

    public AssetFilterDTO() {
        super(FilterType.asset);
    }

    public AssetFilterDTO(Long id, Long reportId) {
        super(FilterType.asset, id, reportId);
    }

    @Builder(builderMethodName = "AssetFilterDTOBuilder")
    public AssetFilterDTO(Long id, Long reportId, String guid, String name) {
        this(id, reportId);
        this.guid = guid;
        this.name = name;
        validate();
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

    @Override
    public Filter convertToFilter() {
        return AssetFilterMapper.INSTANCE.assetFilterDTOToAssetFilter(this);
    }
}

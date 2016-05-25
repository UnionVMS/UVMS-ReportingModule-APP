package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.AssetFilterMapper;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListCriteriaPair;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType.asset;
import static java.util.Arrays.asList;

@Entity
@DiscriminatorValue("ASSET")
@EqualsAndHashCode(callSuper = false, of = {"guid"})
@ToString
public class AssetFilter extends Filter {

    @NotNull
    private String guid;

    @NotNull
    private String name;

    public AssetFilter() {
        super(asset);
    }

    @Builder
    public AssetFilter(String guid, String name) {
        super(asset);
        this.guid = guid;
        this.name = name;
    }

    @Override
    public <T> T accept(FilterVisitor<T> visitor) {
        return visitor.visitAssetFilter(this);
    }

    @Override
    public void merge(Filter filter) {
        AssetFilterMapper.INSTANCE.merge((AssetFilter) filter, this);
    }

    @Override
    public Object getUniqKey() {
        return getGuid();
    }

    @Override
    public List<AssetListCriteriaPair> assetCriteria() {
        return asList(AssetFilterMapper.INSTANCE.assetFilterToAssetListCriteriaPair(this));
    }

    @Override
    public List<ListCriteria> movementListCriteria() {
        return asList(AssetFilterMapper.INSTANCE.assetFilterToListCriteria(this));
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

}

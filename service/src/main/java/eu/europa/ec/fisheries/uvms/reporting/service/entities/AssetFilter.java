package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.AssetFilterMapper;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListCriteriaPair;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@DiscriminatorValue("ASSET")
@EqualsAndHashCode(callSuper = false, of = {"guid"})
@ToString
public class AssetFilter extends Filter {

    private @NotNull String guid;
    private @NotNull String name;

    AssetFilter() {
        super(FilterType.asset);
    }

    @Builder
    public AssetFilter(Long id, String guid, String name) {
        super(FilterType.asset);
        this.guid = guid;
        this.name = name;
        setId(id);
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
        return Lists.newArrayList(AssetFilterMapper.INSTANCE.assetFilterToAssetListCriteriaPair(this));
    }

    @Override
    public List<ListCriteria> movementListCriteria() {
        return Lists.newArrayList(AssetFilterMapper.INSTANCE.assetFilterToListCriteria(this));
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

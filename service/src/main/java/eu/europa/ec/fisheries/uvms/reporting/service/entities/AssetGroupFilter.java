package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.uvms.reporting.service.mapper.*;
import eu.europa.ec.fisheries.wsdl.asset.group.*;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

import static eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType.*;
import static java.util.Arrays.*;

@Entity
@DiscriminatorValue("VGROUP")
@EqualsAndHashCode(callSuper = false, of = {"guid"})
@ToString
public class AssetGroupFilter extends Filter {

    @NotNull
    private String guid;

    @NotNull
    private String name;

    @NotNull
    private String userName;

    public AssetGroupFilter() {
        super(vgroup);
    }

    @Builder
    public AssetGroupFilter(Long id, String groupId, String userName, String name) {
        super(vgroup);
        this.guid = groupId;
        this.userName = userName;
        this.name = name;
        setId(id);
    }

    @Override
    public <T> T accept(FilterVisitor<T> visitor) {
        return visitor.visitAssetGroupFilter(this);
    }

    @Override
    public void merge(Filter filter) {
        AssetGroupFilterMapper.INSTANCE.merge((AssetGroupFilter) filter, this);
    }

    @Override
    public List<AssetGroup> assetGroupCriteria() {
        return asList(AssetGroupFilterMapper.INSTANCE.assetGroupFilterToAssetGroup(this));
    }

    @Override
    public Object getUniqKey() {
        return getGuid();
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

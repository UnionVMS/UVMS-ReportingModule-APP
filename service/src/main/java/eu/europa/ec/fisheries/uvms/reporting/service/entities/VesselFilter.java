package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.FilterVisitor;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.VesselFilterMapper;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselListCriteriaPair;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@DiscriminatorValue("VESSEL")
@EqualsAndHashCode(callSuper = true, of = {"guid", "name"})
public class VesselFilter extends Filter {

    @NotNull
    private String guid;

    @NotNull
    private String name;

    VesselFilter() {
        super(FilterType.vessel);
    }

    @Builder
    public VesselFilter(Long id, String guid, String name) {
        super(FilterType.vessel);
        this.guid = guid;
        this.name = name;
        setId(id);
    }

    @Override
    public <T> T accept(FilterVisitor<T> visitor) {
        return visitor.visitVesselFilter(this);
    }

    @Override
    public void merge(Filter filter) {
        VesselFilterMapper.INSTANCE.merge((VesselFilter) filter, this);
    }

    @Override
    public Object getUniqKey() {
        return hashCode();
    }

    @Override
    public List<VesselListCriteriaPair> vesselCriteria() {
        return Lists.newArrayList(VesselFilterMapper.INSTANCE.vesselFilterToVesselListCriteriaPair(this));
    }

    @Override
    public List<ListCriteria> movementListCriteria() {
        return Lists.newArrayList(VesselFilterMapper.INSTANCE.vesselFilterToListCriteria(this));
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

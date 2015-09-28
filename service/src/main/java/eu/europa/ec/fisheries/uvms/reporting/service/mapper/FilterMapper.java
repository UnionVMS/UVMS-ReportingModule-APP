package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.visitor.DTOToFilterVisitor;
import eu.europa.ec.fisheries.uvms.reporting.service.visitor.FilterToDTOVisitor;

public class FilterMapper {

    public Filter filterToFilterDTO(final FilterDTO filter){
        return filter.accept(new DTOToFilterVisitor());
    }

    public FilterDTO filtersDTOToFilter(final Filter filter){
       return filter.accept(new FilterToDTOVisitor()); // TODO test this
    }
}

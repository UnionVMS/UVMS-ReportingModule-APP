package eu.europa.ec.fisheries.uvms.reporting.service.dao;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.service.AbstractCrudService;

import javax.persistence.EntityManager;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter.LIST_BY_REPORT_ID;
import static eu.europa.ec.fisheries.uvms.service.QueryParameter.with;

public class FilterDAO extends AbstractCrudService {

    private EntityManager em;

    public FilterDAO(EntityManager em){
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @SuppressWarnings("unchecked")
    public List<Filter> listByReportId(final Long reportId){
        return findEntityByNamedQuery(Filter.class, LIST_BY_REPORT_ID, with("reportId", reportId).parameters());
    }
}

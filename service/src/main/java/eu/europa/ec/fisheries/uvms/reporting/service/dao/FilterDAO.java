package eu.europa.ec.fisheries.uvms.reporting.service.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.service.DAO;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter.LIST_BY_ID;
import static eu.europa.ec.fisheries.uvms.service.QueryParameter.with;

public class FilterDAO extends AbstractDAO<Filter> implements DAO<Filter> {

    private EntityManager em;

    public FilterDAO(EntityManager em){
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Filter> listById(final List<Long> filterIds) throws ServiceException {
        List<Filter> returnList = null;
        if (filterIds != null && !filterIds.isEmpty()) {
            returnList = findEntityByNamedQuery(Filter.class, LIST_BY_ID, with("id", filterIds).parameters());
        } else {
            returnList = new ArrayList<>();
        }
        return returnList;
    }
}

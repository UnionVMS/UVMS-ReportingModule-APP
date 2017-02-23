/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.util.merger;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.reporting.dao.FilterDAO;
import eu.europa.ec.fisheries.uvms.reporting.dao.ReportDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.entities.Report;
import eu.europa.ec.fisheries.uvms.service.Merger;
import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class FilterMerger extends Merger<FilterDTO, Filter> {

    private FilterDAO filterDAO;
    private ReportDAO reportDAO;

    public FilterMerger(final EntityManager em) {
        this.filterDAO = new FilterDAO(em);
        this.reportDAO = new ReportDAO(em);
    }

    @Override
    protected Object getUniqKey(final Filter item) throws ServiceException {
        return item.getUniqKey();
    }

    @Override
    protected Collection<Filter> convert(FilterDTO input) throws ServiceException {
        Filter filter = input.convertToFilter();
        Set<Filter> filterSet = new HashSet<>(1);
        filterSet.add(filter);
        return filterSet;
    }

    @Override
    protected Collection<Filter> loadCurrents(Collection<FilterDTO> input) throws ServiceException {
        Iterator<FilterDTO> iterator = input.iterator();
        Long reportId = null;
        while (iterator.hasNext()){
            FilterDTO next = iterator.next();
            reportId = next.getReportId();
            if(reportId != null){
                break;
            }
        }
        return filterDAO.listByReportId(reportId);
    }

    @Override
    protected boolean merge(Filter incoming, Filter existing) throws ServiceException {

        boolean merge = !existing.equals(incoming);

        if (merge){
           incoming.setReport(existing.getReport());
           existing.merge(incoming);
        }

        return merge;
    }

    @Override
    protected void insert(Filter item) throws ServiceException {
        Report entityById = reportDAO.findEntityById(Report.class, item.getReportId());
        item.setReport(entityById);
        filterDAO.createEntity(item);
    }

    @Override
    protected void update(Filter item) throws ServiceException {
        filterDAO.updateEntity(item);
    }

    @Override
    protected void delete(Filter item) throws ServiceException {
        filterDAO.deleteBy(item.getId());
    }
}
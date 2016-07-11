/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.merger;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.ReportDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.ReportMapper;
import eu.europa.ec.fisheries.uvms.service.Merger;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * This class is compare an incoming ReportDTO with the existing Report.
 * If no changes were detected the fields will not be updated
 */
public class ReportMerger extends Merger<ReportDTO, Report> {

    private ReportDAO dao;

    public ReportMerger(EntityManager em) {
        dao = new ReportDAO(em);
    }

    @Override
    protected Object getUniqKey(Report item) throws ServiceException {
        return item.getId();
    }

    @Override
    protected Collection<Report> convert(ReportDTO input) throws ServiceException {

        ReportMapper build = ReportMapper.ReportMapperBuilder().filters(true).build();

        return Arrays.asList(build.reportDTOToReport(input));

    }

    @Override
    @SuppressWarnings("unchecked")
    protected Collection<Report> loadCurrents(final Collection<ReportDTO> input) throws ServiceException {

        Iterator<ReportDTO> iterator = input.iterator();

        Long reportId = null;

        while (iterator.hasNext()){

            ReportDTO next = iterator.next();

            reportId = next.getId();

            if (reportId != null) {

                break;

            }

        }

        return Arrays.asList((Report) dao.findEntityById(Report.class, reportId));

    }

    @Override
    protected boolean merge(Report incoming, Report existing) throws ServiceException {

        boolean merge = !existing.equals(incoming);

        if (merge){
            existing.merge(incoming);
        }

        return merge;
    }

    @Override
    protected void insert(Report item) throws ServiceException {
        //not called
    }

    @Override
    protected void update(Report item) throws ServiceException {
        dao.updateEntity(item);
    }

    @Override
    protected void delete(Report item) throws ServiceException {
        //not called
    }
}
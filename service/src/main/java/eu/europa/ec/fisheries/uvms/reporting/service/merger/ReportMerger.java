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
 * //TODO create test
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
        ReportMapper build = ReportMapper.reportMapperBuilder().filters(true).build();
        return Arrays.asList(build.reportDtoToReport(input));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Collection<Report> loadCurrents(Collection<ReportDTO> input) throws ServiceException {

        Iterator<ReportDTO> iterator = input.iterator();
        Long reportId = null;

        while (iterator.hasNext()){
            ReportDTO next = iterator.next();
            reportId = next.getId();
            if(reportId != null){
                break;
            }
        }

        return Arrays.asList((Report) dao.findEntityById(Report.class, reportId));
    }

    @Override
    protected boolean merge(Report incoming, Report existing) throws ServiceException {

        boolean merge = !existing.equals(incoming);

        if (merge){
            ReportMapper mapper = ReportMapper.reportMapperBuilder().filters(false).build();
            mapper.merge(incoming, existing);
        }

        return merge;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void insert(Report item) throws ServiceException {
        dao.createEntity(item);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void update(Report item) throws ServiceException {
        dao.updateEntity(item);
    }

    @Override
    protected void delete(Report item) throws ServiceException {
        dao.remove(item.getId());
    }
}

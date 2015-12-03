package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.ReportMapper;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@LocalBean
public class ReportServiceHandlerBean {

    @EJB
    private ReportRepository repository;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public ReportDTO saveReport(ReportDTO report) {
        try {
            ReportMapper mapper = ReportMapper.ReportMapperBuilder().filters(true).build();
            Report reportEntity = mapper.reportDTOToReport(report);
            reportEntity = repository.createEntity(reportEntity); // TODO @Greg mapping in repository
            ReportDTO reportDTO = mapper.reportToReportDTO(reportEntity);
            return reportDTO;
        } catch (Exception e) {
            throw new RuntimeException("Error during the creation of the report");
        }
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public boolean updateReport(ReportDTO report) throws ReportingServiceException {
        return repository.update(report);
    }

}

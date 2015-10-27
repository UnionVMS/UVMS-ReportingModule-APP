package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.uvms.exception.ProcessorException;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ExtVesselMessageMapper;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.FilterProcessor;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Stateless
@Local(value = VmsService.class)
public class VmsServiceBean implements VmsService {

    @EJB
    private ReportRepository repository;

    @EJB
    private VesselServiceBean vessel;

    @EJB
    private MovementServiceBean movement;

    @Override
    @Transactional
    public VmsDTO getVmsDataByReportId(final String username, final String scopeName, final Long id) throws ReportingServiceException {

        VmsDTO vmsDto;
        ImmutableMap<String, Vessel> vesselMap;
        Collection<MovementMapResponseType> movementMap;
        Map<String, MovementMapResponseType> responseTypeMap;

        try {

            Report reportByReportId = repository.findReportByReportId(id, username, scopeName);
            validate(id, reportByReportId);

            FilterProcessor processor = new FilterProcessor(reportByReportId.getFilters());

            if (processor.hasVesselsOrVesselGroups()) {
                vesselMap = vessel.getVesselMap(processor);
                movementMap = movement.getMovement(processor);
            } else {
                responseTypeMap = movement.getMovementMap(processor); //FIXME replace with other method if ready
                Set<String> vesselGuids = responseTypeMap.keySet();
                movementMap = responseTypeMap.values();
                processor.getVesselListCriteriaPairs().addAll(ExtVesselMessageMapper.vesselCriteria(vesselGuids));
                vesselMap = vessel.getVesselMap(processor);
            }

            vmsDto = new VmsDTO(vesselMap, movementMap);
            reportByReportId.updateExecutionLog(username);

        } catch (ProcessorException e) {
            throw new ReportingServiceException("Error while processing reporting filters.", e);
        }

        return vmsDto;
    }

    private void validate(Long id, Report reportByReportId) throws ReportingServiceException {
        if (reportByReportId == null) {
            throw new ReportingServiceException("No report found with id " + id);
        }
    }

}
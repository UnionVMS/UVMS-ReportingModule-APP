package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.uvms.exception.ProcessorException;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ExtMovementMessageMapper;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ExtVesselMessageMapper;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.FilterProcessor;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Stateless
@Local(value = VmsService.class)
public class VmsServiceBean implements VmsService {

    @EJB
    private ReportRepository repository;

    @EJB
    private VesselServiceBean vessel;

    @EJB
    private MovementServiceBean movement;
    
    @EJB
    private SpatialService SpatialService;

    @Override
    @Transactional
    public VmsDTO getVmsDataByReportId(final String username, final String scopeName, final Long id) throws ReportingServiceException {

        VmsDTO vmsDto;
        Map<String, Vessel> vesselMap;
        Collection<MovementMapResponseType> movementMap;
        Map<String, MovementMapResponseType> responseTypeMap;

        try {

            Report reportByReportId = repository.findReportByReportId(id, username, scopeName);
            validate(id, reportByReportId);

            FilterProcessor processor = new FilterProcessor(reportByReportId.getFilters());
            addAreaCriteriaToProcessor(processor);            

            if (processor.hasVesselsOrVesselGroups()) {
                vesselMap = vessel.getVesselMap(processor);
                Collection<? extends ListCriteria> listCriteria = ExtMovementMessageMapper.movementListCriteria(vesselMap.keySet());
                List<ListCriteria> movementListCriteria = processor.getMovementListCriteria();
                for(ListCriteria criteria : listCriteria){
                    if (!movementListCriteria.contains(criteria)){
                        movementListCriteria.add(criteria);
                    }
                }
                movementMap = movement.getMovement(processor);
            } else {
                responseTypeMap = movement.getMovementMap(processor);
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
    
    private void addAreaCriteriaToProcessor(FilterProcessor processor) throws ReportingServiceException {
    	if (isNotEmpty(processor.getAreaIdentifierList())) {
    		String areaWkt = getFilterArea(processor.getAreaIdentifierList());
        	processor.addAreaCriteria(areaWkt);
    	}    	
    }
    
	private String getFilterArea(List<AreaIdentifierType> areaIdentifierList) throws ReportingServiceException {
		try {
			return SpatialService.getFilterArea(areaIdentifierList);
		} catch (ReportingServiceException e) {
			throw new ReportingServiceException("Exception during retrieving filter area");
		}
	}

    private void validate(Long id, Report reportByReportId) throws ReportingServiceException {
        if (reportByReportId == null) {
            throw new ReportingServiceException("No report found with id " + id);
        }
    }

}
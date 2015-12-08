package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.uvms.common.AuditActionEnum;
import eu.europa.ec.fisheries.uvms.exception.ProcessorException;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ExtMovementMessageMapper;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ExtVesselMessageMapper;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.FilterProcessor;
import eu.europa.ec.fisheries.uvms.service.interceptor.IAuditInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.ArrayList;
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
    private VesselServiceBean vesselModule;

    @EJB
    private MovementServiceBean movementModule;
    
    @EJB
    private SpatialService spatialModule;

    @Override
    @Transactional
    @IAuditInterceptor(auditActionType = AuditActionEnum.EXECUTE)
    public VmsDTO getVmsDataByReportId(final String username, final String scopeName, final Long id) throws ReportingServiceException {

        Report reportByReportId = repository.findReportByReportId(id, username, scopeName);

        if (reportByReportId == null) {

            throw new ReportingServiceException("No report found with id " + id);

        }

        VmsDTO vmsDto;

        try {

            Map<String, Vessel> vesselMap;

            FilterProcessor processor = new FilterProcessor(reportByReportId.getFilters());

            addAreaCriteriaToProcessor(processor);

            Collection<MovementMapResponseType> movementMap;

            Map<String, MovementMapResponseType> responseTypeMap;

            if (processor.hasVesselsOrVesselGroups()) {

                vesselMap = vesselModule.getVesselMap(processor);

                processor.getMovementListCriteria().addAll(ExtMovementMessageMapper.movementListCriteria(vesselMap.keySet()));

                movementMap = movementModule.getMovement(processor);

            } else {

                responseTypeMap = movementModule.getMovementMap(processor);

                Set<String> vesselGuids = responseTypeMap.keySet();

                movementMap = responseTypeMap.values();

                processor.getVesselListCriteriaPairs().addAll(ExtVesselMessageMapper.vesselCriteria(vesselGuids));

                vesselMap = vesselModule.getVesselMap(processor);
            }

            vmsDto = new VmsDTO(vesselMap, movementMap);

            reportByReportId.updateExecutionLog(username);

        } catch (ProcessorException e) {

            throw new ReportingServiceException("Error while processing reporting filters.", e);

        }

        return vmsDto;

    }

    private void addAreaCriteriaToProcessor(FilterProcessor processor) throws ReportingServiceException {

        final Set<AreaIdentifierType> areaIdentifierList = processor.getAreaIdentifierList();

        if (isNotEmpty(areaIdentifierList)) {

    		String areaWkt = getFilterArea(areaIdentifierList);

        	processor.addAreaCriteria(areaWkt);

    	}

    }
    
	private String getFilterArea(Set<AreaIdentifierType> areaIdentifierList) throws ReportingServiceException {

		try {

            List<AreaIdentifierType> areaIdentifierTypeList = new ArrayList<>();

            areaIdentifierTypeList.addAll(areaIdentifierList);

			return spatialModule.getFilterArea(areaIdentifierTypeList);

		} catch (ReportingServiceException e) {

			throw new ReportingServiceException("Exception during retrieving filter area", e);

		}

	}

}
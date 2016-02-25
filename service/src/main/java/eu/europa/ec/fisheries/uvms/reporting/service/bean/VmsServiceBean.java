package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.uvms.common.AuditActionEnum;
import eu.europa.ec.fisheries.uvms.exception.ProcessorException;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ExtAssetMessageMapper;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ExtMovementMessageMapper;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.FilterProcessor;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.ReportMapperV2;
import eu.europa.ec.fisheries.uvms.rest.security.bean.USMService;
import eu.europa.ec.fisheries.uvms.service.interceptor.IAuditInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;

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
@Slf4j
public class VmsServiceBean implements VmsService {

    @EJB
    private ReportRepository repository;

    @EJB
    private AuditService auditService;


    @EJB
    private AssetServiceBean assetModule;

    @EJB
    private MovementServiceBean movementModule;

    @EJB
    private SpatialService spatialModule;

    @EJB
    private USMService usmService;

    @Override
    @Transactional
    @IAuditInterceptor(auditActionType = AuditActionEnum.EXECUTE)
    public VmsDTO getVmsDataByReportId(final String username, final String scopeName, final Long id, final List<AreaIdentifierType> areaRestrictions) throws ReportingServiceException {

        log.debug("[START] getVmsDataByReportId({}, {}, {})", username, scopeName, id);

        Report reportByReportId = repository.findReportByReportId(id, username, scopeName);

        if (reportByReportId == null) {

            String error = MessageFormatter.arrayFormat("No report found with id {}", new Object[]{id}).getMessage();

            log.error(error);

            throw new ReportingServiceException(error);

        }

        VmsDTO vmsDto = getVmsData(reportByReportId, areaRestrictions);

        reportByReportId.updateExecutionLog(username);

        log.debug("[END] getVmsDataByReportId(...)");

        return vmsDto;

    }

    private VmsDTO getVmsData(Report report, List<AreaIdentifierType> areaRestrictions) throws ReportingServiceException {

        try {

            Map<String, Asset> assetMap;

            FilterProcessor processor = new FilterProcessor(report.getFilters());

            if (areaRestrictions != null) {
                processor.getScopeRestrictionAreaIdentifierList().addAll(areaRestrictions);
            }

            addAreaCriteriaToProcessor(processor);

            Collection<MovementMapResponseType> movementMap;

            Map<String, MovementMapResponseType> responseTypeMap;

            log.debug("Running report {} assets or asset groups.", processor.hasAssetsOrAssetGroups() ? "has" : "doesn't have");

            if (processor.hasAssetsOrAssetGroups()) {

                assetMap = assetModule.getAssetMap(processor);

                processor.getMovementListCriteria().addAll(ExtMovementMessageMapper.movementListCriteria(assetMap.keySet()));

                movementMap = movementModule.getMovement(processor);

            } else {

                responseTypeMap = movementModule.getMovementMap(processor);

                Set<String> assetGuids = responseTypeMap.keySet();

                movementMap = responseTypeMap.values();

                processor.getAssetListCriteriaPairs().addAll(ExtAssetMessageMapper.assetCriteria(assetGuids));

                assetMap = assetModule.getAssetMap(processor);
            }

            return new VmsDTO(assetMap, movementMap);

        } catch (ProcessorException e) {

            String error = "Error while processing reporting filters";

            log.error(error, e);

            throw new ReportingServiceException(error, e);

        }

    }

    @Override
    public VmsDTO getVmsDataBy(final eu.europa.ec.fisheries.uvms.reporting.model.vms.Report report, final List<AreaIdentifierType> areaRestrictions) throws ReportingServiceException {

        VmsDTO vmsData = getVmsData(ReportMapperV2.INSTANCE.reportDtoToReport(report), areaRestrictions);

        auditService.sendAuditReport(AuditActionEnum.EXECUTE, report.getName());

        return vmsData;

    }

    private void addAreaCriteriaToProcessor(FilterProcessor processor) throws ReportingServiceException {

        final Set<AreaIdentifierType> areaIdentifierList = processor.getAreaIdentifierList();
        final Set<AreaIdentifierType> scopeAreaIdentifierList = processor.getScopeRestrictionAreaIdentifierList();

        try {
            if (presentAreasToFilter(areaIdentifierList, scopeAreaIdentifierList)) {
                String areaWkt = spatialModule.getFilterArea(scopeAreaIdentifierList, areaIdentifierList);
                processor.addAreaCriteria(areaWkt);
            }
        } catch (ReportingServiceException e) {
            String error = "Exception during retrieving filter area";

            log.error(error, e);

            throw new ReportingServiceException(error, e);
        }
    }

    //TODO We are blocking call to spatial to not make unnecessary JMS traffic and calculations
    private boolean presentAreasToFilter(Set<AreaIdentifierType> areaIdentifierList, Set<AreaIdentifierType> scopeAreaIdentifierList) {
        return isNotEmpty(areaIdentifierList) || isNotEmpty(scopeAreaIdentifierList);
    }


}
package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.reporting.message.service.ReportingModuleReceiverBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.SpatialProducerBean;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MapConfigurationDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.MapConfigMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMapperException;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.SpatialModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.SpatialModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.NotImplementedException;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.Collections;
import java.util.List;

@Stateless
@Local(SpatialService.class)
@Slf4j
public class SpatialServiceBean implements SpatialService {

    @EJB
    private SpatialProducerBean spatialProducerBean;

    @EJB
    private ReportingModuleReceiverBean reportingJMSConsumerBean;

    @Override
    public String getFilterArea(List<AreaIdentifierType> userAreas) throws ReportingServiceException {
        try {
            String correlationId = spatialProducerBean.sendModuleMessage(getFilterAreaRequest(userAreas), reportingJMSConsumerBean.getDestination());
            Message message = reportingJMSConsumerBean.getMessage(correlationId, TextMessage.class);
            return getFilterAreaResponse(message, correlationId);
        } catch (SpatialModelMapperException | MessageException | JMSException e) {
            throw new ReportingServiceException(e);
        }
    }

    @Override
    public String getFilterArea(List<AreaIdentifierType> scopeAreas, List<AreaIdentifierType> userAreas) throws ReportingServiceException {
        throw new NotImplementedException("Not implemented");
    }

    @Override
    public MapConfigurationDTO getMapConfiguration(long reportId) throws ReportingServiceException {
        try {
            String getMapConfigurationRequest = createGetMapConfigurationRequest(reportId);
            String correlationId = spatialProducerBean.sendModuleMessage(getMapConfigurationRequest, reportingJMSConsumerBean.getDestination());
            Message message = reportingJMSConsumerBean.getMessage(correlationId, TextMessage.class);

            SpatialGetMapConfigurationRS getMapConfigurationResponse = createGetMapConfigurationResponse(message, correlationId);
            return MapConfigMapper.INSTANCE.mapConfigurationTypeToMapConfigurationDTO(getMapConfigurationResponse.getMapConfiguration());
        } catch (SpatialModelMapperException | MessageException | JMSException e) {
            throw new ReportingServiceException(e);
        }
    }

    @Override
    public boolean saveOrUpdateMapConfiguration(long reportId, MapConfigurationDTO mapConfiguration) throws ReportingServiceException {
        try {
            validate(mapConfiguration);

            CoordinatesFormat coordinatesFormat = (mapConfiguration.getCoordinatesFormat() != null) ? CoordinatesFormat.fromValue(mapConfiguration.getCoordinatesFormat()) : null;
            ScaleBarUnits scaleBarUnits = (mapConfiguration.getScaleBarUnits() != null) ? ScaleBarUnits.fromValue(mapConfiguration.getScaleBarUnits()) : null;

            String request = getSaveMapConfigurationRequest(reportId, mapConfiguration.getSpatialConnectId(), mapConfiguration.getMapProjectionId(), mapConfiguration.getDisplayProjectionId(), coordinatesFormat, scaleBarUnits);
            String correlationId = spatialProducerBean.sendModuleMessage(request, reportingJMSConsumerBean.getDestination());
            Message message = reportingJMSConsumerBean.getMessage(correlationId, TextMessage.class);
            SpatialSaveOrUpdateMapConfigurationRS saveOrUpdateMapConfigurationResponse = getSaveOrUpdateMapConfigurationResponse(message, correlationId);

            return saveOrUpdateMapConfigurationResponse != null;
        } catch (SpatialModelMapperException | MessageException | JMSException e) {
            throw new ReportingServiceException("ERROR DURING SAVE OR UPDATE MAP CONFIG", e);
        }
    }

    @Override
    public void deleteMapConfiguration(List<Long> spatialConnectIds) throws ReportingServiceException {
        try {
            validateSpatialConnectIdsList(spatialConnectIds);

            String request = getDeleteMapConfigurationRequest(spatialConnectIds);
            String correlationId = spatialProducerBean.sendModuleMessage(request, reportingJMSConsumerBean.getDestination());
            Message message = reportingJMSConsumerBean.getMessage(correlationId, TextMessage.class);
            SpatialDeleteMapConfigurationRS deleteMapConfigurationResponse = getDeleteMapConfigurationResponse(message, correlationId);
            log.debug("deleteMapConfiguration response received {}", deleteMapConfigurationResponse.getResponse());

        } catch (SpatialModelMapperException | MessageException | JMSException e) {
            throw new ReportingServiceException("ERROR DURING DELETE MAP CONFIG", e);
        }
    }

    private void validateSpatialConnectIdsList(List<Long> spatialConnectIds) {
        if (CollectionUtils.isEmpty(spatialConnectIds)) {
            throw new IllegalArgumentException("At least one spatial connect id should be specified");
        }
    }

    private void validate(MapConfigurationDTO mapConfiguration) {
        if (mapConfiguration == null) {
            throw new IllegalArgumentException("MAP CONFIGURATION CAN NOT BE NULL");
        }
    }

    private String createGetMapConfigurationRequest(long reportId) throws SpatialModelMarshallException {
        return SpatialModuleRequestMapper.mapToSpatialGetMapConfigurationRQ(reportId);
    }

    private String getDeleteMapConfigurationRequest(List<Long> spatialConnectIds) throws SpatialModelMarshallException {
        return SpatialModuleRequestMapper.mapToSpatialDeleteMapConfigurationRQ(spatialConnectIds);
    }

    private String getSaveMapConfigurationRequest(long reportId, Long spatialConnectId, Long mapProjectionId, Long displayProjectionId, CoordinatesFormat coordinatesFormat, ScaleBarUnits scaleBarUnits) throws SpatialModelMarshallException {
        return SpatialModuleRequestMapper.mapToSpatialSaveOrUpdateMapConfigurationRQ(reportId, spatialConnectId, mapProjectionId, displayProjectionId, coordinatesFormat, scaleBarUnits);
    }

    private SpatialSaveOrUpdateMapConfigurationRS getSaveOrUpdateMapConfigurationResponse(Message message, String correlationId) throws SpatialModelMapperException, JMSException {
        return SpatialModuleResponseMapper.mapToSpatialSaveOrUpdateMapConfigurationRS(getText(message), correlationId);
    }

    private SpatialDeleteMapConfigurationRS getDeleteMapConfigurationResponse(Message message, String correlationId) throws SpatialModelMapperException, JMSException {
        return SpatialModuleResponseMapper.mapToSpatialDeleteMapConfigurationRS(getText(message), correlationId);
    }

    private String getFilterAreaRequest(List<AreaIdentifierType> userAreas) throws SpatialModelMarshallException {
        return SpatialModuleRequestMapper.mapToFilterAreaSpatialRequest(Collections.<AreaIdentifierType>emptyList(), userAreas);
    }

    private String getFilterAreaResponse(Message message, String correlationId) throws SpatialModelMapperException, JMSException {
        FilterAreasSpatialRS filterAreaSpatialResponse = SpatialModuleResponseMapper.mapToFilterAreasSpatialRSFromResponse(getText(message), correlationId);
        return filterAreaSpatialResponse.getGeometry();
    }

    private SpatialGetMapConfigurationRS createGetMapConfigurationResponse(Message message, String correlationId) throws JMSException, SpatialModelMapperException {
        return SpatialModuleResponseMapper.mapToSpatialGetMapConfigurationRS(getText(message), correlationId);
    }

    private TextMessage getText(Message message) throws JMSException {
        if (message instanceof TextMessage) {
            return (TextMessage) message;
        }
        return null;
    }
}

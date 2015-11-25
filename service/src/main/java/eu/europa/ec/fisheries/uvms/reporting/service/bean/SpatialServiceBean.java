package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.reporting.message.service.ReportingJMSConsumerBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.SpatialProducerBean;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MapConfigurationDTO;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMapperException;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.SpatialModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.SpatialModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
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
public class SpatialServiceBean implements SpatialService {

    @EJB
    private SpatialProducerBean spatialProducerBean;

    @EJB
    private ReportingJMSConsumerBean reportingJMSConsumerBean;

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
    public SpatialSaveMapConfigurationRS saveMapConfiguration(Long reportId, MapConfigurationDTO mapConfiguration) throws ReportingServiceException {
        try {
            validate(mapConfiguration);

            Long mapProjection = Long.valueOf(mapConfiguration.getMapProjection());
            Long displayProjection = Long.valueOf(mapConfiguration.getDisplayProjection());
            CoordinatesFormat coordinatesFormat = null;
            if (mapConfiguration.getCoordinatesFormat() != null) {
                coordinatesFormat = CoordinatesFormat.fromValue(mapConfiguration.getCoordinatesFormat().toUpperCase());
            }
            ScaleBarUnits scaleBarUnits = null;
            if (mapConfiguration.getScaleBarUnits() != null) {
                scaleBarUnits = ScaleBarUnits.fromValue(mapConfiguration.getScaleBarUnits().toUpperCase());
            }
            String correlationId = spatialProducerBean.sendModuleMessage(getSaveMapConfigurationRequest(reportId, mapProjection, displayProjection, coordinatesFormat, scaleBarUnits), reportingJMSConsumerBean.getDestination());
            Message message = reportingJMSConsumerBean.getMessage(correlationId, TextMessage.class);
            return getSaveMapConfigurationResponse(message, correlationId);
        } catch (SpatialModelMapperException | MessageException | JMSException e) {
            throw new ReportingServiceException(e);
        }
    }

    private void validate(MapConfigurationDTO mapConfiguration) {
        if (mapConfiguration.getCoordinatesFormat() == null && mapConfiguration.getDisplayProjection() == null && mapConfiguration.getMapProjection() == null && mapConfiguration.getScaleBarUnits() == null) {
            throw new IllegalArgumentException("At least one map configuration attribute should be specified");
        }
    }

    private String getSaveMapConfigurationRequest(Long reportId, Long mapProjection, Long displayProjection, CoordinatesFormat coordinatesFormat, ScaleBarUnits scaleBarUnits) throws SpatialModelMarshallException {
        return SpatialModuleRequestMapper.mapToSpatialSaveMapConfigurationRQ(reportId, mapProjection, displayProjection, coordinatesFormat, scaleBarUnits);
    }

    private SpatialSaveMapConfigurationRS getSaveMapConfigurationResponse(Message message, String correlationId) throws SpatialModelMapperException, JMSException {
        return SpatialModuleResponseMapper.mapToSpatialSaveMapConfigurationRS(getText(message), correlationId);
    }

    private String getFilterAreaRequest(List<AreaIdentifierType> userAreas) throws SpatialModelMarshallException {
        return SpatialModuleRequestMapper.mapToFilterAreaSpatialRequest(Collections.<AreaIdentifierType>emptyList(), userAreas);
    }

    private String getFilterAreaResponse(Message message, String correlationId) throws SpatialModelMapperException, JMSException {
        FilterAreasSpatialRS filterAreaSpatialResponse = SpatialModuleResponseMapper.mapToFilterAreasSpatialRSFromResponse(getText(message), correlationId);
        return filterAreaSpatialResponse.getGeometry();
    }

    private TextMessage getText(Message message) throws JMSException {
        if (message instanceof TextMessage) {
            return (TextMessage) message;
        }
        return null;
    }
}

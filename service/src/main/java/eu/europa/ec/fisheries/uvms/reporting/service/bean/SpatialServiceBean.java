package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.reporting.message.service.ReportingJMSConsumerBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.SpatialProducerBean;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MapConfigurationDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.MapConfigMapper;
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
    public MapConfigurationDTO getMapConfiguration(Long reportId) throws ReportingServiceException {
        try {
            validateReportId(reportId);

            String correlationId = spatialProducerBean.sendModuleMessage(createGetConfigurationRequest(reportId), reportingJMSConsumerBean.getDestination());
            Message message = reportingJMSConsumerBean.getMessage(correlationId, TextMessage.class);

            SpatialGetMapConfigurationRS getMapConfigurationResponse = createGetMapConfigurationResponse(message, correlationId);

            MapConfigurationType mapConfigurationType = getMapConfigurationResponse.getMapConfiguration();
            return MapConfigMapper.INSTANCE.mapConfigurationTypeToMapConfigurationDTO(mapConfigurationType);
        } catch (SpatialModelMapperException | MessageException | JMSException e) {
            throw new ReportingServiceException(e);
        }
    }

    private void validateReportId(Long reportId) {
        if (reportId == null) {
            throw new IllegalArgumentException("Report Id must be specified.");
        }
    }

    @Override
    public boolean saveOrUpdateMapConfiguration(long reportId, MapConfigurationDTO mapConfiguration) throws ReportingServiceException {
        try {
            validate(mapConfiguration);

            Long spatialConnectId = mapConfiguration.getSpatialConnectId();
            Long mapProjectionId = mapConfiguration.getMapProjectionId();
            Long displayProjectionId = mapConfiguration.getDisplayProjectionId();
            CoordinatesFormat coordinatesFormat = null;
            if (mapConfiguration.getCoordinatesFormat() != null) {
                coordinatesFormat = CoordinatesFormat.fromValue(mapConfiguration.getCoordinatesFormat().toUpperCase());
            }
            ScaleBarUnits scaleBarUnits = null;
            if (mapConfiguration.getScaleBarUnits() != null) {
                scaleBarUnits = ScaleBarUnits.fromValue(mapConfiguration.getScaleBarUnits().toUpperCase());
            }

            String request = getSaveMapConfigurationRequest(reportId, spatialConnectId, mapProjectionId, displayProjectionId, coordinatesFormat, scaleBarUnits);
            String correlationId = spatialProducerBean.sendModuleMessage(request, reportingJMSConsumerBean.getDestination());
            Message message = reportingJMSConsumerBean.getMessage(correlationId, TextMessage.class);
            SpatialSaveOrUpdateMapConfigurationRS saveOrUpdateMapConfigurationResponse = getSaveOrUpdateMapConfigurationResponse(message, correlationId);

            return saveOrUpdateMapConfigurationResponse != null;
        } catch (SpatialModelMapperException | MessageException | JMSException e) {
            throw new ReportingServiceException("ERROR DURING SAVE OR UPDATE MAP CONFIG", e);
        }
    }

    private void validate(MapConfigurationDTO mapConfiguration) {
        if (mapConfiguration == null) {
            throw new IllegalArgumentException("MAP CONFIGURATION CAN NOT BE NULL");
        }
        if (mapConfiguration.getCoordinatesFormat() == null && mapConfiguration.getDisplayProjectionId() == null && mapConfiguration.getMapProjectionId() == null && mapConfiguration.getScaleBarUnits() == null) {
            throw new IllegalArgumentException("At least one map configuration attribute should be specified");
        }
    }

    private String createGetConfigurationRequest(Long reportId) throws SpatialModelMarshallException {
        return SpatialModuleRequestMapper.mapToSpatialGetMapConfigurationRQ(reportId);
    }

    private String getSaveMapConfigurationRequest(long reportId, Long spatialConnectId, Long mapProjectionId, Long displayProjectionId, CoordinatesFormat coordinatesFormat, ScaleBarUnits scaleBarUnits) throws SpatialModelMarshallException {
        return SpatialModuleRequestMapper.mapToSpatialSaveOrUpdateMapConfigurationRQ(reportId, spatialConnectId, mapProjectionId, displayProjectionId, coordinatesFormat, scaleBarUnits);
    }

    private SpatialSaveOrUpdateMapConfigurationRS getSaveOrUpdateMapConfigurationResponse(Message message, String correlationId) throws SpatialModelMapperException, JMSException {
        return SpatialModuleResponseMapper.mapToSpatialSaveOrUpdateMapConfigurationRS(getText(message), correlationId);
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

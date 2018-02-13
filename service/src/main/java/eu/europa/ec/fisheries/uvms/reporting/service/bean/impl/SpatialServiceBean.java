/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.service.bean.impl;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.reporting.message.service.ReportingModuleReceiverBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.SpatialProducerBean;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.SpatialService;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.LayerSettingsDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MapConfigurationDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReferenceDataPropertiesDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.StyleSettingsDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VisibilitySettingsDto;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.MapConfigMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMapperException;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.SpatialModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.SpatialModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.CoordinatesFormat;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.FilterAreasSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LayerSettingsType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ReferenceDataType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ScaleBarUnits;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialDeleteMapConfigurationRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialGetMapConfigurationRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.StyleSettingsType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.VisibilitySettingsType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

@Stateless
@Local(SpatialService.class)
@Slf4j
public class SpatialServiceBean implements SpatialService {

    @EJB
    private SpatialProducerBean spatialProducerBean;

    @EJB
    private ReportingModuleReceiverBean reportingJMSConsumerBean;

    @Override
    public String getFilterArea(Set<AreaIdentifierType> scopeAreas, Set<AreaIdentifierType> userAreas) throws ReportingServiceException {
        try {
            List<AreaIdentifierType> scopeAreasList = new ArrayList<>();
            List<AreaIdentifierType> userAreasList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(scopeAreas)) {
                scopeAreasList.addAll(scopeAreas);
            }
            if (CollectionUtils.isNotEmpty(userAreas)) {
                userAreasList.addAll(userAreas);
            }
            String correlationId = spatialProducerBean.sendModuleMessage(SpatialModuleRequestMapper.mapToFilterAreaSpatialRequest(scopeAreasList, userAreasList), reportingJMSConsumerBean.getDestination());
            TextMessage message = reportingJMSConsumerBean.getMessage(correlationId, TextMessage.class);
            return getFilterAreaResponse(message, correlationId);
        } catch (SpatialModelMapperException | MessageException e) {
            throw new ReportingServiceException(e);
        }
    }

    @Override
    public MapConfigurationDTO getMapConfiguration(long reportId, List<String> permittedServiceLayers) throws ReportingServiceException {
        try {
            String getMapConfigurationRequest = createGetMapConfigurationRequest(reportId, permittedServiceLayers);
            String correlationId = spatialProducerBean.sendModuleMessage(getMapConfigurationRequest, reportingJMSConsumerBean.getDestination());
            TextMessage message = reportingJMSConsumerBean.getMessage(correlationId, TextMessage.class);
            SpatialGetMapConfigurationRS getMapConfigurationResponse = createGetMapConfigurationResponse(message, correlationId);
            MapConfigurationDTO mapConfigurationDTO = MapConfigMapper.INSTANCE.mapConfigurationTypeToMapConfigurationDTO(getMapConfigurationResponse.getMapConfiguration());
            VisibilitySettingsDto visibilitySettingsDto = MapConfigMapper.INSTANCE.getVisibilitySettingsDto(getMapConfigurationResponse.getMapConfiguration().getVisibilitySettings());
            StyleSettingsDto styleSettingsDto = MapConfigMapper.INSTANCE.getStyleSettingsDto(getMapConfigurationResponse.getMapConfiguration().getStyleSettings());
            LayerSettingsDto layerSettingsDto = MapConfigMapper.INSTANCE.getLayerSettingsDto(getMapConfigurationResponse.getMapConfiguration().getLayerSettings());
            Map<String, ReferenceDataPropertiesDto> referenceData = MapConfigMapper.INSTANCE.getReferenceData(getMapConfigurationResponse.getMapConfiguration().getReferenceDatas());
            mapConfigurationDTO.setVisibilitySettings(visibilitySettingsDto);
            mapConfigurationDTO.setStyleSettings(styleSettingsDto);
            mapConfigurationDTO.setLayerSettings(validateLayerSettings(layerSettingsDto));
            mapConfigurationDTO.setReferenceData(referenceData);
            return mapConfigurationDTO;
        } catch (SpatialModelMapperException | MessageException e) {
            throw new ReportingServiceException(e);
        }
    }

    @Override
    public boolean saveOrUpdateMapConfiguration(long reportId, MapConfigurationDTO mapConfiguration) throws ReportingServiceException {
        try {
            validate(mapConfiguration);
            CoordinatesFormat coordinatesFormat = (mapConfiguration.getCoordinatesFormat() != null) ? CoordinatesFormat.fromValue(mapConfiguration.getCoordinatesFormat()) : null;
            ScaleBarUnits scaleBarUnits = (mapConfiguration.getScaleBarUnits() != null) ? ScaleBarUnits.fromValue(mapConfiguration.getScaleBarUnits()) : null;
            VisibilitySettingsType visibilitySettingsType = MapConfigMapper.INSTANCE.getVisibilitySettingsType(mapConfiguration.getVisibilitySettings());
            StyleSettingsType styleSettingsType = MapConfigMapper.INSTANCE.getStyleSettingsType(mapConfiguration.getStyleSettings());
            LayerSettingsType layerSettingsType = MapConfigMapper.INSTANCE.getLayerSettingsType(mapConfiguration.getLayerSettings());
            List<ReferenceDataType> referenceDataType = MapConfigMapper.INSTANCE.getReferenceDataType(mapConfiguration.getReferenceData());
            String request = getSaveMapConfigurationRequest(reportId, mapConfiguration.getSpatialConnectId(),
                    mapConfiguration.getMapProjectionId(), mapConfiguration.getDisplayProjectionId(), coordinatesFormat, scaleBarUnits,
                    styleSettingsType, visibilitySettingsType, layerSettingsType, referenceDataType);
            String correlationId = spatialProducerBean.sendModuleMessage(request, reportingJMSConsumerBean.getDestination());
            TextMessage message = reportingJMSConsumerBean.getMessage(correlationId, TextMessage.class);
            SpatialSaveOrUpdateMapConfigurationRS saveOrUpdateMapConfigurationResponse = getSaveOrUpdateMapConfigurationResponse(message, correlationId);
            return saveOrUpdateMapConfigurationResponse != null;
        } catch (SpatialModelMapperException | MessageException e) {
            throw new ReportingServiceException("ERROR DURING SAVE OR UPDATE MAP CONFIG", e);
        }
    }

    @Override
    public void deleteMapConfiguration(List<Long> spatialConnectIds) throws ReportingServiceException {
        try {
            validateSpatialConnectIdsList(spatialConnectIds);
            String request = getDeleteMapConfigurationRequest(spatialConnectIds);
            String correlationId = spatialProducerBean.sendModuleMessage(request, reportingJMSConsumerBean.getDestination());
            TextMessage message = reportingJMSConsumerBean.getMessage(correlationId, TextMessage.class);
            SpatialDeleteMapConfigurationRS deleteMapConfigurationResponse = getDeleteMapConfigurationResponse(message, correlationId);
            log.debug("deleteMapConfiguration response received {}", deleteMapConfigurationResponse.getResponse());
        } catch (SpatialModelMapperException | MessageException e) {
            throw new ReportingServiceException("ERROR DURING DELETE MAP CONFIG", e);
        }
    }

    private LayerSettingsDto validateLayerSettings(LayerSettingsDto layerSettingsDto) {
        if (layerSettingsDto.getBaseLayers() == null
                && layerSettingsDto.getAdditionalLayers() == null
                && layerSettingsDto.getAreaLayers() == null
                && layerSettingsDto.getPortLayers() == null) {
            return null;
        }
        return layerSettingsDto;
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

    private String createGetMapConfigurationRequest(long reportId, List<String> permittedServiceLayers) throws SpatialModelMarshallException {
        return SpatialModuleRequestMapper.mapToSpatialGetMapConfigurationRQ(reportId, permittedServiceLayers);
    }

    private String getDeleteMapConfigurationRequest(List<Long> spatialConnectIds) throws SpatialModelMarshallException {
        return SpatialModuleRequestMapper.mapToSpatialDeleteMapConfigurationRQ(spatialConnectIds);
    }

    private String getSaveMapConfigurationRequest(long reportId,
                                                  Long spatialConnectId,
                                                  Long mapProjectionId,
                                                  Long displayProjectionId,
                                                  CoordinatesFormat coordinatesFormat,
                                                  ScaleBarUnits scaleBarUnits,
                                                  StyleSettingsType styleSettingsType,
                                                  VisibilitySettingsType visibilitySettingsType,
                                                  LayerSettingsType layerSettingsType,
                                                  List<ReferenceDataType> referenceDataType) throws SpatialModelMarshallException {
        return SpatialModuleRequestMapper.mapToSpatialSaveOrUpdateMapConfigurationRQ(reportId, spatialConnectId,
                mapProjectionId, displayProjectionId, coordinatesFormat, scaleBarUnits,
                styleSettingsType, visibilitySettingsType, layerSettingsType, referenceDataType);
    }

    private SpatialSaveOrUpdateMapConfigurationRS getSaveOrUpdateMapConfigurationResponse(TextMessage message, String correlationId) throws SpatialModelMapperException {
        return SpatialModuleResponseMapper.mapToSpatialSaveOrUpdateMapConfigurationRS(message, correlationId);
    }

    private SpatialDeleteMapConfigurationRS getDeleteMapConfigurationResponse(TextMessage message, String correlationId) throws SpatialModelMapperException {
        return SpatialModuleResponseMapper.mapToSpatialDeleteMapConfigurationRS(message, correlationId);
    }

    private String getFilterAreaResponse(TextMessage message, String correlationId) throws SpatialModelMapperException {
        FilterAreasSpatialRS filterAreaSpatialResponse = SpatialModuleResponseMapper.mapToFilterAreasSpatialRSFromResponse(message, correlationId);
        return filterAreaSpatialResponse.getGeometry();
    }

    private SpatialGetMapConfigurationRS createGetMapConfigurationResponse(TextMessage message, String correlationId) throws SpatialModelMapperException {
        return SpatialModuleResponseMapper.mapToSpatialGetMapConfigurationRS(message, correlationId);
    }

}
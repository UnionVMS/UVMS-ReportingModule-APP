/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.reporting.service.dto.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.europa.ec.fisheries.uvms.commons.date.CustomDateSerializer;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.AuditDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ExecutionLogDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MapConfigurationDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.enums.ReportTypeEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.util.ReportDeserializer;
import eu.europa.ec.fisheries.uvms.reporting.service.util.ReportSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonDeserialize(using = ReportDeserializer.class)
@JsonSerialize(using = ReportSerializer.class)
@EqualsAndHashCode(of = {"createdBy", "scopeName", "id", "description", "withMap", "visibility", "name", "deletable", "editable", "isDeleted"})
public class ReportDTO {

    private Long id;
    private String name;
    private String description;
    private boolean withMap = true;
    private String scopeName;
    private String createdBy;
    private List<VisibilityEnum> shareable;
    private boolean editable = true;
    private boolean deletable;
    private AuditDTO audit = new AuditDTO();
    private VisibilityEnum visibility = VisibilityEnum.PRIVATE;
    private boolean isDeleted;
    private boolean isDefault;
    private ReportTypeEnum reportTypeEnum = ReportTypeEnum.STANDARD;
    private Integer mapZoom;
    private String mapCenter;
    private String mapLayerConfig;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date deletedOn;

    private String deletedBy;

    private List<FilterDTO> filters = new ArrayList<>();

    private ExecutionLogDTO executionLog;

    private MapConfigurationDTO mapConfiguration = new MapConfigurationDTO();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getWithMap() {
        return withMap;
    }

    public void setWithMap(boolean withMap) {
        this.withMap = withMap;
    }

    public String getScopeName() {
        return scopeName;
    }

    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<VisibilityEnum> isShareable() {
        return shareable;
    }

    public void setShareable(List<VisibilityEnum> shareable) {
        this.shareable = shareable;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    public AuditDTO getAudit() {
        return audit;
    }

    public void setAudit(AuditDTO audit) {
        this.audit = audit;
    }

    public VisibilityEnum getVisibility() {
        return visibility;
    }

    public void setVisibility(VisibilityEnum visibility) {
        this.visibility = visibility;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Date getDeletedOn() {
        Date newDate = null;
        if (this.deletedOn != null){
            newDate = new Date(this.deletedOn.getTime());
        }
        return newDate;
    }

    public void setDeletedOn(Date deletedOn) {
        if (deletedOn != null){
            this.deletedOn = new Date(deletedOn.getTime());
        }
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public List<FilterDTO> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterDTO> filters) {
        this.filters = filters;
    }

    public ExecutionLogDTO getExecutionLog() {
        return executionLog;
    }

    public void setExecutionLog(ExecutionLogDTO executionLog) {
        this.executionLog = executionLog;
    }

    public MapConfigurationDTO getMapConfiguration() {
        return mapConfiguration;
    }

    public void setMapConfiguration(MapConfigurationDTO mapConfiguration) {
        this.mapConfiguration = mapConfiguration;
    }

    public ReportTypeEnum getReportTypeEnum() {
        return reportTypeEnum;
    }

    public void setReportTypeEnum(ReportTypeEnum reportTypeEnum) {
        this.reportTypeEnum = reportTypeEnum;
    }

    public void addFilter(FilterDTO filter) {
        filters.add(filter);
    }

    public Integer getMapZoom() {
        return mapZoom;
    }

    public void setMapZoom(Integer mapZoom) {
        this.mapZoom = mapZoom;
    }

    public String getMapCenter() {
        return mapCenter;
    }

    public void setMapCenter(String mapCenter) {
        this.mapCenter = mapCenter;
    }

    public String getMapLayerConfig() {
        return mapLayerConfig;
    }

    public void setMapLayerConfig(String mapLayerConfig) {
        this.mapLayerConfig = mapLayerConfig;
    }
}
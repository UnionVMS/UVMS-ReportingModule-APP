/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.europa.ec.fisheries.uvms.reporting.model.ReportTypeEnum;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.ReportDTODeserializer;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.ReportDTOSerializer;
import eu.europa.ec.fisheries.uvms.rest.serializer.CustomDateSerializer;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonDeserialize(using = ReportDTODeserializer.class)
@JsonSerialize(using = ReportDTOSerializer.class)
@EqualsAndHashCode(of = {"description", "withMap", "visibility", "name", "deletable", "editable", "filters", "isDeleted"})
public class ReportDTO implements Serializable {

    public static final String DESC = "desc";
    public static final String CREATED_BY = "createdBy";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String WITH_MAP = "withMap";
    public static final String IS_DEFAULT = "isDefault";
    public static final String CREATED_ON = "createdOn";
    public static final String SCOPE_ID = "scopeId";
    public static final String VISIBILITY = "visibility";
    public static final String REPORT_TYPE = "reportType";
    public static final String FILTER_EXPRESSION = "filterExpression";
    public static final String SHAREABLE = "shareable";
    public static final String DELETABLE = "deletable";
    public static final String EDITABLE = "editable";
    public static final String MAP_CONFIGURATION = "mapConfiguration";

    private Long id;
    private String name;
    private String description;
    private Boolean withMap;
    private String scopeName;
    private String createdBy;
    private List<VisibilityEnum> shareable;
    private boolean editable;
    private boolean deletable;
    private AuditDTO audit;
    private VisibilityEnum visibility;
    private boolean isDeleted;
    private Boolean isDefault = false;
    private ReportTypeEnum reportTypeEnum;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date deletedOn;

    private String deletedBy;

    private List<FilterDTO> filters = new ArrayList<>();

    private ExecutionLogDTO executionLog;

    private MapConfigurationDTO mapConfiguration;

    public ReportDTO() {
    }

    @Builder(builderMethodName = "ReportDTOBuilder")
    public ReportDTO(Long id,
                     String name,
                     String description,
                     Boolean withMap,
                     String scopeName,
                     String createdBy,
                     VisibilityEnum visibility,
                     boolean isDeleted,
                     Date createdOn,
                     Date deletedOn,
                     String deletedBy,
                     ReportTypeEnum reportTypeEnum,
                     List<FilterDTO> filters,
                     MapConfigurationDTO mapConfiguration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.withMap = withMap;
        this.scopeName = scopeName;
        this.createdBy = createdBy;
        this.visibility = visibility;
        this.isDeleted = isDeleted;
        this.deletedOn = deletedOn;
        this.deletedBy = deletedBy;
        this.filters = filters;
        if (this.audit == null) {
            this.audit = new AuditDTO();
        }
        this.audit.setCreatedOn(createdOn);
        this.mapConfiguration = mapConfiguration;
        this.reportTypeEnum = reportTypeEnum;
    }

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

    public Boolean getWithMap() {
        return withMap;
    }

    public void setWithMap(Boolean withMap) {
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

    public void setDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Date getDeletedOn() {
        Date deletedOn = null;
        if (this.deletedOn != null){
            deletedOn = new Date(this.deletedOn.getTime());
        }
        return deletedOn;
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
}
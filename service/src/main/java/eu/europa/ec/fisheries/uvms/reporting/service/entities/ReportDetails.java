/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.converter.CharBooleanConverter;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.ReportDetailsMapper;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Embeddable
@EqualsAndHashCode
@ToString
public class ReportDetails {

    public static final String CREATED_BY = "created_by";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String WITH_MAP = "with_map";
    public static final String SCOPE_NAME = "scope_name";
    public static final int LENGTH_255 = 255;
    public static final int LENGTH_1 = 1;

    @NotNull
    @Column(nullable = false, length = LENGTH_255)
    private String name;

    @Column(nullable = true, length = LENGTH_255)
    private String description;

    @Column(name = WITH_MAP, nullable = false, length = LENGTH_1)
    @Convert(converter = CharBooleanConverter.class)
    private Boolean withMap;

    @NotNull
    @Column(name = SCOPE_NAME)
    private String scopeName;

    @NotNull
    @Column(name = CREATED_BY, nullable = false, length = LENGTH_255)
    private String createdBy;

    ReportDetails() {

    }

    @Builder
    public ReportDetails(String description, String name, Boolean withMap, String scopeName, String createdBy) {
        this.name = name;
        this.withMap = withMap;
        this.scopeName = scopeName;
        this.createdBy = createdBy;
        this.description = description;
    }

    public void merge(ReportDetails incoming) {
        ReportDetailsMapper.INSTANCE.merge(incoming, this);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
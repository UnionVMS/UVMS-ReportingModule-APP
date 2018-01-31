/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.converter.CharBooleanConverter;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.ReportDetailsMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDetails implements Serializable{

    public static final String CREATED_BY = "created_by";
    public static final String NAME_ = "name";
    public static final String DESCRIPTION_ = "description";
    public static final String WITH_MAP = "with_map";
    public static final String SCOPE_NAME = "scope_name";

    @NotNull
    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "with_map", nullable = false, length = 1)
    @Convert(converter = CharBooleanConverter.class)
    private Boolean withMap;

    @NotNull
    @Column(name = "scope_name")
    private String scopeName;

    @NotNull
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    public void merge(ReportDetails incoming) {
        ReportDetailsMapper.INSTANCE.merge(incoming, this);
    }

}
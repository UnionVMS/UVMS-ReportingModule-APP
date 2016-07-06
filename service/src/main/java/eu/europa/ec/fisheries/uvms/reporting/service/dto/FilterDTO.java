/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.FilterType;
import lombok.EqualsAndHashCode;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode( of = {"id"})
public abstract class FilterDTO {

    public static final String ID = "id";
    public static final String TYPE = "type";

    protected Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @JsonIgnore
    private Long reportId;

    private Long id;

    private FilterType type;

    public FilterDTO(FilterType type) {
        this.type = type;
    }

    public FilterDTO(FilterType type, Long id, Long reportId) {
        this(type);
        this.id = id;
        this.reportId = reportId;
    }

    public abstract Filter convertToFilter();

    public void validate() {
        Set<ConstraintViolation<FilterDTO>> violations =
                validator.validate(this);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(
                    new HashSet<ConstraintViolation<?>>(violations));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FilterType getType() {
        return type;
    }

    public void setType(FilterType type) {
        this.type = type;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }
}
/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.util.validation;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.CommonFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.PositionSelector;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Selector;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CommonFilterValidator implements ConstraintValidator<CommonFilterIsValid, CommonFilter> {

    @Override
    public void initialize(CommonFilterIsValid commonFilterIsValid) {
    }

    @Override
    public boolean isValid(CommonFilter commonFilter, ConstraintValidatorContext constraintValidatorContext) {

        boolean valid = true;

        PositionSelector positionSelector = commonFilter.getPositionSelector();

        if (positionSelector != null && Selector.all.equals(positionSelector.getSelector())){
            valid = positionSelector.getPosition() == null &&
                    positionSelector.getValue() == null;
        }

        return valid;
    }
}
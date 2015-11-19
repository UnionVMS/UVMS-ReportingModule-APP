package eu.europa.ec.fisheries.uvms.reporting.service.validation;

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

        else if (positionSelector != null && Selector.last.equals(positionSelector.getSelector())){
            valid = commonFilter.getEndDate() == null &&
                    commonFilter.getStartDate() == null;
        }

        return valid;
    }
}

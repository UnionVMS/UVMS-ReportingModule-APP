package eu.europa.ec.fisheries.uvms.reporting.service.helper;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleSegmentType;

import java.util.Comparator;

/**
 * Created by padhyad on 3/29/2016.
 */
public class CustomRuleSegmentTypeComparator implements Comparator<CustomRuleSegmentType> {
    @Override
    public int compare(CustomRuleSegmentType one, CustomRuleSegmentType two) {
        Integer orderOne = Integer.valueOf(one.getOrder());
        Integer orderTwo = Integer.valueOf(two.getOrder());
        return orderOne.compareTo(orderTwo);
    }
}

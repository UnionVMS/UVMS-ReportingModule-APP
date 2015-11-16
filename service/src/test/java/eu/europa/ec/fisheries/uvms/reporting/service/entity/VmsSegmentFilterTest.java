package eu.europa.ec.fisheries.uvms.reporting.service.entity;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeKeyType;
import eu.europa.ec.fisheries.schema.movement.search.v1.SearchKey;
import eu.europa.ec.fisheries.schema.movement.v1.SegmentCategoryType;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsSegmentFilter;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsSegmentFilter.VmsSegmentFilterBuilder;
import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertEquals;
import static org.unitils.reflectionassert.ReflectionAssert.assertLenientEquals;

@RunWith(JUnitParamsRunner.class)
public class VmsSegmentFilterTest {

    @Test
    @Parameters(method = "criteriaValues")
    public void shouldReturnListCriteria(VmsSegmentFilter filter, List<ListCriteria> expected){

        assertLenientEquals(expected, filter.movementCriteria());

    }

    @Test
    @Parameters(method = "rangeCriteriaValues")
    public void shouldReturnListRangeCriteria(VmsSegmentFilter filter, List<RangeCriteria> expected){

        assertLenientEquals(expected, filter.movementRangeCriteria());

    }

    @Test
    public void shouldReturnSameAfterMerge(){

        Filter segmentFilter = VmsSegmentFilterBuilder()
                .category(SegmentCategoryType.ANCHORED)
                .maxDuration(100F).minDuration(10F)
                .maximumSpeed(50F).minimumSpeed(5F)
                .build();
        Filter merged = VmsSegmentFilterBuilder().build();

        segmentFilter.merge(merged);

        assertEquals(merged, segmentFilter);

    }

    protected Object[] criteriaValues(){

        Filter filter =
                VmsSegmentFilterBuilder().category(SegmentCategoryType.ANCHORED).build();

        ListCriteria listCriteria = new ListCriteria();
        listCriteria.setKey(SearchKey.CATEGORY);
        listCriteria.setValue(SegmentCategoryType.ANCHORED.value());

        return $(
                $(filter, Arrays.asList(listCriteria))
        );
    }

    protected Object[] rangeCriteriaValues(){

        Filter filter1 = VmsSegmentFilterBuilder().minDuration(12F).maxDuration(13F).build();
        RangeCriteria rangeCriteria = new RangeCriteria();
        rangeCriteria.setKey(RangeKeyType.SEGMENT_DURATION);
        rangeCriteria.setFrom("12.0");
        rangeCriteria.setTo("13.0");

        Filter filter2 = VmsSegmentFilterBuilder().minimumSpeed(6.45F).maximumSpeed(15.5F).build();
        RangeCriteria rangeCriteria2 = new RangeCriteria();
        rangeCriteria2.setKey(RangeKeyType.SEGMENT_SPEED);
        rangeCriteria2.setFrom("6.45");
        rangeCriteria2.setTo("15.5");

        return $(
            $(filter1, Arrays.asList(rangeCriteria)),
            $(filter2, Arrays.asList(rangeCriteria2))
        );
    }
}

package eu.europa.ec.fisheries.uvms.reporting.service.dto;


import eu.europa.ec.fisheries.uvms.reporting.service.entities.Selector;
import org.joda.time.DateTime;
import org.junit.Test;

import static eu.europa.ec.fisheries.uvms.reporting.service.dto.CommonFilterDTO.CommonFilterDTOBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.PositionSelectorDTO.PositionSelectorDTOBuilder;
import static junit.framework.TestCase.assertTrue;

public class CommonFilterDTOTest {

    @Test
    public void testEquals(){

        CommonFilterDTO build = CommonFilterDTOBuilder()
                .endDate(new DateTime(2004, 3, 26, 12, 1, 1, 1).toDate())
                .startDate(new DateTime(2005, 3, 26, 12, 1, 1, 1).toDate())
                .reportId(45L)
                .positionSelector(PositionSelectorDTOBuilder().selector(Selector.ALL).build())
                .build();

        CommonFilterDTO build2 = CommonFilterDTOBuilder()
                .endDate(new DateTime(2004, 3, 26, 12, 1, 1, 1).toDate())
                .startDate(new DateTime(2005, 3, 26, 12, 1, 1, 1).toDate())
                .reportId(38L)
                .positionSelector(PositionSelectorDTOBuilder().selector(Selector.ALL).build())
                .build();

        assertTrue(build.equals(build2));
    }
}

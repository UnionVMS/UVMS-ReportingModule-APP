package eu.europa.ec.fisheries.uvms.reporting.service.entity;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.MockUnitils;
import org.unitils.mock.PartialMock;
import org.unitils.mock.annotation.Dummy;

public class ReportTest extends UnitilsJUnit4 {

    @TestedObject
    private PartialMock<Report> report;

    @Dummy
    private Report dummy;

    @Test
    public void testMerge(){

        report.getMock().merge(dummy);

        report.assertInvoked().setId(null);
        report.assertInvoked().setName(null);
        report.assertInvoked().setDescription(null);
        report.assertInvoked().setWithMap(null);
        report.assertInvoked().setIsDeleted(null);
        report.assertInvoked().setDeletedBy(null);
        report.assertInvoked().setVisibility(null);

        MockUnitils.assertNoMoreInvocations();

    }
}

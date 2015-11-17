package eu.europa.ec.fisheries.uvms.reporting.service.merger;

import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.ReportDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import lombok.SneakyThrows;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;

import static eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO.ReportDTOBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.entities.Report.ReportBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.entities.ReportDetails.ReportDetailsBuilder;
import static junit.framework.TestCase.assertTrue;

public class ReportMergerTest extends UnitilsJUnit4 {

    @PersistenceContext
    private EntityManager em;

    @TestedObject
    private ReportMerger merger = new ReportMerger(em);

    @InjectIntoByType
    private Mock<ReportDAO> daoMock;

    @Test
    @SneakyThrows
        public void shouldUpdateWhenValuesModified(){

        Report existingReport = Report.builder().id(1L).details(ReportDetailsBuilder().description("desc").createdBy("me").build()).build();
        ReportDTO incomingReport = ReportDTOBuilder().id(1L).createdBy("you").description("desc").build();

        daoMock.returns(existingReport).findEntityById(Report.class, null);

        boolean updated = merger.merge(Arrays.asList(incomingReport));

        daoMock.assertInvoked().updateEntity(existingReport);
        daoMock.assertNotInvoked().deleteEntity(existingReport, 1L);
        daoMock.assertNotInvoked().createEntity(existingReport);

        assertTrue(updated);

    }

    @Test
    @SneakyThrows
    public void shouldNotUpdateWhenNothingHasChanged(){

        Report existingReport = Report.builder().id(1L).details(ReportDetailsBuilder().description("desc").createdBy("you").build()).build();
        ReportDTO incomingReport = ReportDTOBuilder().id(1L).
                visibility(VisibilityEnum.PRIVATE).createdBy("you").description("desc").build();

        daoMock.returns(existingReport).findEntityById(Report.class, null);

        boolean updated = merger.merge(Arrays.asList(incomingReport));

        daoMock.assertNotInvoked().updateEntity(existingReport);
        daoMock.assertNotInvoked().deleteEntity(existingReport, 1L);
        daoMock.assertNotInvoked().createEntity(existingReport);

        assertTrue(!updated);

    }
}

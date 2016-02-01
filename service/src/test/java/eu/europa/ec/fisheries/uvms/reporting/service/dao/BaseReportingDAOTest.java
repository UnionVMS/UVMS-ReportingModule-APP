package eu.europa.ec.fisheries.uvms.reporting.service.dao;


import com.ninja_squad.dbsetup.operation.Operation;
import eu.europa.ec.fisheries.uvms.dao.BaseDAOTest;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;

public class BaseReportingDAOTest extends BaseDAOTest {

    protected static final Operation DELETE_ALL = sequenceOf(
            deleteAllFrom("reporting.filter"),
            deleteAllFrom("reporting.report")
            );

    protected static final Operation INSERT_REFERENCE_DATA =
            sequenceOf();

    @Override
    protected String getSchema() {
        return "reporting";
    }

    @Override
    protected String getPersistenceUnitName() {
        return "testPU";
    }

}

package eu.europa.ec.fisheries.uvms.reporting.model.mappper;

import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingModelException;
import eu.europa.ec.fisheries.uvms.reporting.model.schemas.ReportGetStartAndEndDateRQ;
import eu.europa.ec.fisheries.uvms.reporting.model.schemas.ReportingModuleMethod;

/**
 * Created by padhyad on 3/18/2016.
 */
public class ReportingModuleRequestMapper {

    public static String mapToSpatialSaveOrUpdateMapConfigurationRQ(String now, Long id, String userName, String scopeName) throws ReportingModelException {
        try {
            ReportGetStartAndEndDateRQ request = new ReportGetStartAndEndDateRQ(ReportingModuleMethod.GET_REPORT_START_END_DATE, now, id, userName, scopeName);
            return JAXBMarshaller.marshall(request);
        } catch (ReportingModelException ex) {
            throw new ReportingModelException("[ Error when marshalling Object to String ]", ex);
        }
    }
}

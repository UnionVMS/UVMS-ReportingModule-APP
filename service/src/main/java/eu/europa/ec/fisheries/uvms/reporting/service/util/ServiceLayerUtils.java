package eu.europa.ec.fisheries.uvms.reporting.service.util;

import eu.europa.ec.fisheries.uvms.rest.security.bean.USMService;
import eu.europa.ec.fisheries.uvms.spatial.model.constants.USMSpatial;
import eu.europa.ec.fisheries.wsdl.user.types.Dataset;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by padhyad on 6/24/2016.
 */
public class ServiceLayerUtils {

    public static Collection<String> getUserPermittedLayersNames(USMService usmService, String username, String roleName, String scopeName) throws eu.europa.ec.fisheries.uvms.exception.ServiceException {
        //String category, String username, String applicationName, String currentRole, String currentScope
        List<Dataset> permittedServiceLayerDatasets = usmService.getDatasetsPerCategory(USMSpatial.CATEGORY_SERVICE_LAYER, username, USMSpatial.APPLICATION_NAME, roleName, scopeName);

        Collection<String> permittedLayersNames = new HashSet<>(permittedServiceLayerDatasets.size());
        for(Dataset dataset : permittedServiceLayerDatasets) {
            permittedLayersNames.add(dataset.getName());
        }

        return permittedLayersNames;
    }
}

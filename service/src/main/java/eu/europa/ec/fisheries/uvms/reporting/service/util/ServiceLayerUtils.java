/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
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

    public static Collection<String> getUserPermittedLayersNames(USMService usmService, String username, String roleName, String scopeName) throws eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException {
        //String category, String username, String applicationName, String currentRole, String currentScope
        List<Dataset> permittedServiceLayerDatasets = usmService.getDatasetsPerCategory(USMSpatial.CATEGORY_SERVICE_LAYER, username, USMSpatial.APPLICATION_NAME, roleName, scopeName);

        Collection<String> permittedLayersNames = new HashSet<>(permittedServiceLayerDatasets.size());
        for(Dataset dataset : permittedServiceLayerDatasets) {
            permittedLayersNames.add(dataset.getName());
        }

        return permittedLayersNames;
    }
}
/*
 *
 *  Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2020.
 *
 *  This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 *  and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 *  the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 *  details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *  
 */

/*
 *
 *  Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2020.
 *
 *  This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 *  and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 *  the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 *  details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package eu.europa.ec.fisheries.uvms.reporting.service.bean.impl;

import eu.europa.ec.fisheries.schema.movement.common.v1.SimpleResponse;
import eu.europa.ec.fisheries.schema.movement.module.v1.CreateMovementBatchResponse;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.IncomingMovementDataService;
import eu.europa.ec.fisheries.uvms.reporting.service.exception.MessageFormatException;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.xml.bind.JAXBException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class IncomingMovementDataServiceImpl implements IncomingMovementDataService {

    @EJB
    private MovementServiceBean movementServiceBean;
    
    @Override
    public void handle(String message) throws ReportingServiceException {
        CreateMovementBatchResponse messageObj = unmarshal(message);
        if(messageObj.getResponse() == SimpleResponse.OK){
            List<MovementType> movementTypes = messageObj.getMovements().stream()
                    .filter(m -> !m.isDuplicate())
                    .collect(Collectors.toList());
            movementServiceBean.createMovementsSegmentsAndTracks(movementTypes);
        }
    }
    
    private CreateMovementBatchResponse unmarshal(String message) {
        try {
            return JAXBUtils.unMarshallMessage(message, CreateMovementBatchResponse.class);
        } catch (JAXBException e) {
            throw new MessageFormatException(e);
        }
    }
}

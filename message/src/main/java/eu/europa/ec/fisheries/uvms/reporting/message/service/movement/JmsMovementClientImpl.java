/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.reporting.message.service.movement;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.List;

import eu.europa.ec.fisheries.schema.movement.module.v1.GetSegmentsAndTrackBySegmentIdsRequest;
import eu.europa.ec.fisheries.schema.movement.module.v1.GetSegmentsAndTrackBySegmentIdsResponse;
import eu.europa.ec.fisheries.schema.movement.module.v1.MovementModuleMethod;
import eu.europa.ec.fisheries.schema.movement.v1.SegmentAndTrackList;
import eu.europa.ec.fisheries.schema.movement.v1.SegmentIds;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.movement.model.exception.MovementModelException;
import eu.europa.ec.fisheries.uvms.movement.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.reporting.message.service.MovementModuleSenderBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.MovementResponseConsumerBean;

@ApplicationScoped
public class JmsMovementClientImpl implements MovementClient {

    private MovementModuleSenderBean movementModuleSenderBean;
    private MovementResponseConsumerBean movementResponseConsumerBean;

    /**
     * Injection constructor.
     *
     * @param movementModuleSenderBean     The (JMS) producer bean for this module
     * @param movementResponseConsumerBean The queue
     */
    @Inject
    public JmsMovementClientImpl(MovementModuleSenderBean movementModuleSenderBean, MovementResponseConsumerBean movementResponseConsumerBean) {
        this.movementModuleSenderBean = movementModuleSenderBean;
        this.movementResponseConsumerBean = movementResponseConsumerBean;
    }

    /**
     * Constructor for frameworks.
     */
    @SuppressWarnings("unused")
    JmsMovementClientImpl() {
        // NOOP
    }

    @Override
    public List<SegmentAndTrackList> getSegmentsAndTrackBySegmentIds(List<SegmentIds> segmentIds) {
        GetSegmentsAndTrackBySegmentIdsResponse resp = null;
        String corrId;
        try {
            GetSegmentsAndTrackBySegmentIdsRequest request = new GetSegmentsAndTrackBySegmentIdsRequest();
            request.setMethod(MovementModuleMethod.GET_SEGMENTS_AND_TRACK_BY_SEGMENT_IDS);
            request.getSegmentIds().addAll(segmentIds);

            corrId = movementModuleSenderBean.sendMessageToSpecificQueue(
                    JAXBMarshaller.marshallJaxBObjectToString(request),
                    movementModuleSenderBean.getDestination(),
                    movementResponseConsumerBean.getDestination());

            if (corrId != null) {
                TextMessage responseMessage = movementResponseConsumerBean.getMessage(corrId, TextMessage.class);
                resp = JAXBUtils.unMarshallMessage(responseMessage.getText(), GetSegmentsAndTrackBySegmentIdsResponse.class);
            }

        } catch (MessageException | MovementModelException | JMSException | JAXBException e) {
            e.printStackTrace();
        }
        return resp != null ? resp.getSegmentAndTrackList() : new ArrayList<>();
    }
}

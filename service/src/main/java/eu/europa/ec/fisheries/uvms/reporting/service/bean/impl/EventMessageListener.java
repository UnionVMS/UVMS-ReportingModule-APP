/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.reporting.service.bean.impl;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.IncomingActivityDataService;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.IncomingAssetDataService;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.IncomingMovementDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MessageDriven(mappedName = "jms/topic/EventMessageTopic", activationConfig = {
        @ActivationConfigProperty(propertyName = MessageConstants.MESSAGE_SELECTOR_STR, propertyValue = "mainTopic" + " = '" + "reporting" + "'"),
        @ActivationConfigProperty(propertyName = MessageConstants.MESSAGING_TYPE_STR, propertyValue = MessageConstants.CONNECTION_TYPE),
        @ActivationConfigProperty(propertyName = MessageConstants.SUBSCRIPTION_DURABILITY_STR, propertyValue = MessageConstants.DURABLE_CONNECTION),
        @ActivationConfigProperty(propertyName = MessageConstants.DESTINATION_TYPE_STR, propertyValue = MessageConstants.DESTINATION_TYPE_TOPIC),
        @ActivationConfigProperty(propertyName = MessageConstants.DESTINATION_STR, propertyValue = "EventMessageTopic"),
        @ActivationConfigProperty(propertyName = MessageConstants.DESTINATION_JNDI_NAME, propertyValue = "jms/topic/EventMessageTopic"),
        @ActivationConfigProperty(propertyName = MessageConstants.SUBSCRIPTION_NAME_STR, propertyValue = "SubscriptionForReporting"),
        @ActivationConfigProperty(propertyName = MessageConstants.CLIENT_ID_STR, propertyValue = "REPORTING_MODULE"),
        @ActivationConfigProperty(propertyName = MessageConstants.CONNECTION_FACTORY_JNDI_NAME, propertyValue = MessageConstants.CONNECTION_FACTORY)
})
public class EventMessageListener implements MessageListener {

    final static Logger LOG = LoggerFactory.getLogger(EventMessageListener.class);

    private static final String ACTIVITY_SUB_TOPIC = "activity";
    private static final String MOVEMENT_SUB_TOPIC = "movement";
    private static final String ASSET_SUB_TOPIC = "asset";

    @Inject
    private IncomingActivityDataService incomingActivityDataService;

    @Inject
    private IncomingMovementDataService incomingMovementDataService;

    @Inject
    private IncomingAssetDataService incomingAssetDataService;

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            String subTopic = message.getStringProperty("subTopic");

            switch (subTopic) {
                case ACTIVITY_SUB_TOPIC:
                    incomingActivityDataService.handle(textMessage.getText());
                    break;
                case MOVEMENT_SUB_TOPIC:
                    incomingMovementDataService.handle(textMessage.getText());
                    break;
                case ASSET_SUB_TOPIC:
                    incomingAssetDataService.handle(textMessage.getText());
                    break;
                default:
                    LOG.error("Unknown message sub topic");
            }
        } catch (JMSException | ReportingServiceException e) {
            LOG.error("An error occured in message processing", e);
        }
    }

}
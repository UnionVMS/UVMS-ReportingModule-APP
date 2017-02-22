/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.message.service;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;

import eu.europa.ec.fisheries.uvms.message.AbstractMessageService;

import static eu.europa.ec.fisheries.uvms.message.MessageConstants.*;

/**
 * AuditMessageServiceBean responsible to send an receive messages to and from AUDIT module from Reporting
 * The implementation of sending message to the queue is handled in generic implementation of AbstractProducer
 * 
 * @see {@link AbstractMessageService}
 *
 */
@Stateless
@LocalBean
public class AuditMessageServiceBean extends AbstractMessageService {

    public static final String MODULE_NAME = "reporting";

    @Resource(mappedName = QUEUE_AUDIT_EVENT)
    private Destination request;

    @Resource(mappedName = QUEUE_AUDIT)
    private Destination response;

    @Resource(lookup = CONNECTION_FACTORY)
    private ConnectionFactory connectionFactory;

    @Override
    protected ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    @Override
    protected Destination getEventDestination() {
        return request;
    }

    @Override
    protected Destination getResponseDestination() {
        return response;
    }

    @Override
    protected String getModuleName() {
        return MODULE_NAME;
    }

}
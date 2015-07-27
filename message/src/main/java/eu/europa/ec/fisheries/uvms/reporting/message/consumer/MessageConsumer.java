/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.europa.ec.fisheries.uvms.reporting.message.consumer;

import javax.ejb.Local;

/**
 *
 * @author jojoha
 */
@Local
public interface MessageConsumer {

    public <T> T getMessage(String correlationId, Class type) throws Exception;

}

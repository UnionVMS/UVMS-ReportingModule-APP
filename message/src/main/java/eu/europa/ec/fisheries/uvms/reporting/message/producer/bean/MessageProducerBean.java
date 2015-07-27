/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.europa.ec.fisheries.uvms.reporting.message.producer.bean;

import eu.europa.ec.fisheries.uvms.reporting.message.producer.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
//import eu.europa.ec.fisheries.uvms.reporting.model.exception.ModelMarshallException;
//import eu.europa.ec.fisheries.uvms.reporting.model.mapper.JAXBMarshaller;

@Stateless
public class MessageProducerBean implements MessageProducer {

    final static Logger LOG = LoggerFactory.getLogger(MessageProducerBean.class);
/*

    @Resource(mappedName = MessageConstants.QUEUE_DATASOURCE_INTERNAL)
    private Queue localDbQueue;

    @Resource(mappedName = MessageConstants.COMPONENT_RESPONSE_QUEUE)
    private Queue responseQueue;

    @Resource(mappedName = MessageConstants.QUEUE_MODULE_VESSEL)
    private Queue vesselQueue;

    @Resource(lookup = MessageConstants.CONNECTION_FACTORY)
    private ConnectionFactory connectionFactory;

    private Connection connection = null;
    private Session session = null;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendDataSourceMessage(String text, DataSourceQueue queue) throws MovementMessageException {
        try {

            LOG.info("[ Sending datasource message to recipient on queue {} ] ", queue.name());

            connectToQueue();
            TextMessage message = session.createTextMessage();
            message.setJMSReplyTo(responseQueue);
            message.setText(text);

            switch (queue) {
                case INTERNAL:
                    session.createProducer(localDbQueue).send(message);
                    break;
                case INTEGRATION:
                    break;
                default:
                    break;
            }

            return message.getJMSMessageID();
        } catch (Exception e) {
            LOG.error("[ Error when sending message. ] {}", e.getMessage());
            throw new MovementMessageException("[ Error when sending message. ]", e);
        } finally {
            try {
                connection.stop();
                connection.close();
            } catch (JMSException e) {
                LOG.error("[ Error when closing JMS connection ] {}", e.getStackTrace());
                throw new MovementMessageException("[ Error when sending message. ]", e);
            }
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendErrorMessageBackToRecipient(@Observes @ErrorEvent EventMessage message) throws MovementMessageException {
        try {

            connectToQueue();
            TextMessage requestMessage = message.getJmsMessage();

            ExceptionType exception = new ExceptionType();
            exception.setCode(BigInteger.ZERO);
            exception.setFault(message.getErrorMessage());

            MovementException exceptionMessage = new MovementException(message.getErrorMessage(), exception);
            String text = JAXBMarshaller.marshallJaxBObjectToString(exceptionMessage);

            TextMessage replyMessage = session.createTextMessage();
            replyMessage.setJMSDestination(requestMessage.getJMSReplyTo());
            replyMessage.setText(text);

            LOG.info("[ Sending error message back to recipient on queue ] {}", requestMessage.getJMSReplyTo());

            session.createProducer(replyMessage.getJMSDestination()).send(replyMessage);
        } catch (JMSException | ModelMarshallException e) {
            LOG.error("[ Error when sending message. ] {}", e.getMessage());
            throw new MovementMessageException("[ Error when sending message. ]", e);
        } finally {
            try {
                connection.stop();
                connection.close();
            } catch (JMSException e) {
                LOG.error("[ Error when closing JMS connection ] {}", e.getStackTrace());
                throw new MovementMessageException("[ Error when sending message. ]", e);
            }
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendMessageBackToRecipient(TextMessage requestMessage, String returnMessage) throws MovementMessageException {
        try {

            LOG.info("[ Sending message back to recipient on queue ] {}", requestMessage.getJMSReplyTo());

            connectToQueue();
            TextMessage message = session.createTextMessage();
            message.setJMSDestination(requestMessage.getJMSReplyTo());
            message.setText(returnMessage);

            session.createProducer(message.getJMSDestination()).send(message);

        } catch (Exception e) {
            LOG.error("[ Error when sending message. ] {}", e.getMessage());
            throw new MovementMessageException("[ Error when sending message. ]", e);
        } finally {
            try {
                connection.stop();
                connection.close();
            } catch (JMSException e) {
                LOG.error("[ Error when closing JMS connection ] {}", e.getStackTrace());
                throw new MovementMessageException("[ Error when sending message. ]", e);
            }
        }
    }

    private void connectToQueue() throws JMSException {
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        connection.start();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendModuleMessage(String text, ModuleQueue queue) throws MovementMessageException {
        try {

            connectToQueue();

            TextMessage message = session.createTextMessage();
            message.setJMSReplyTo(responseQueue);
            message.setText(text);

            switch (queue) {
                case VESSEL:
                    session.createProducer(vesselQueue).send(message);
                    break;
            }

            return message.getJMSMessageID();
        } catch (Exception e) {
            LOG.error("[ Error when sending data source message. ] {}", e.getMessage());
            throw new MovementMessageException(e.getMessage());
        } finally {
            try {
                connection.stop();
                connection.close();
            } catch (JMSException e) {
                LOG.error("[ Error when closing JMS connection ] {}", e.getStackTrace());
                throw new MovementMessageException("[ Error when sending message. ]", e);
            }
        }
    }
*/

}

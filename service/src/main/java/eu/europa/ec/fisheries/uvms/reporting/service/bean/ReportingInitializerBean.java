package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.init.AbstractModuleInitializerBean;
import eu.europa.ec.fisheries.uvms.message.MessageConstants;
import eu.europa.ec.fisheries.uvms.message.MessageConsumer;
import eu.europa.ec.fisheries.uvms.reporting.message.service.ReportingJMSConsumerBean;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.jms.Destination;
import javax.jms.Queue;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//@Singleton
//@Startup
public class ReportingInitializerBean extends AbstractModuleInitializerBean {

    @Override
    protected InputStream getDeploymentDescriptorRequest() {
        return this.getClass().getClassLoader().getResourceAsStream("usmDeploymentDescriptor.xml");
    }



}
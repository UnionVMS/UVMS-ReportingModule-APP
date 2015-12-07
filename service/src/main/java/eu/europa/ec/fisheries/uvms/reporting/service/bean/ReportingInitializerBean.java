package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.init.AbstractModuleInitializerBean;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.InputStream;

@Singleton
@Startup
public class ReportingInitializerBean extends AbstractModuleInitializerBean {

    @Override
    protected InputStream getDeploymentDescriptorRequest() {
        return this.getClass().getClassLoader().getResourceAsStream("usmDeploymentDescriptor.xml");
    }



}
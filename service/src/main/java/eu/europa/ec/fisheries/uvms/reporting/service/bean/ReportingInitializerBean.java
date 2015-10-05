package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.init.AbstractModuleInitializerBean;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Singleton
@Startup
public class ReportingInitializerBean extends AbstractModuleInitializerBean {

    @Override
    protected Properties retrieveModuleConfigs() throws IOException {
        Properties prop = new Properties();
        InputStream properties = getClass().getClassLoader().getResourceAsStream("config.properties");
        if (properties != null) {
            prop.load(properties);
            return prop;
        } else {
            throw new FileNotFoundException("Property file config.properties not found in the classpath");
        }
    }

    @Override
    protected InputStream getDeploymentDescriptor() {
        return this.getClass().getClassLoader().getResourceAsStream("usmDeploymentDescriptor.xml");
    }
}
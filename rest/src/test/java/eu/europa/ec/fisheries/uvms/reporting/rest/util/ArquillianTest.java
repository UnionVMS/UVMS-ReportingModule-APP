/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.rest.util;

import java.io.File;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;

public class ArquillianTest {

	 @Deployment(testable = false)
    public static WebArchive createDeployment() {
    	WebArchive war = ShrinkWrap.create(WebArchive.class,"reporting.war").addPackages(true, "eu.europa.ec.fisheries.uvms.reporting.rest")
                //.addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource( new File( "src/main/webapp/WEB-INF/web.xml" ) )
                .addAsManifestResource(new File( "src/test/resources/META-INF/jboss-deployment-structure.xml"))
                .addAsResource("config.properties")
                .addAsResource("logback.xml")
                .addAsWebInfResource(new File("src/test/webapp/WEB-INF/beans.xml"));
        File[] libs = Maven.resolver().loadPomFromFile("pom.xml")
                .importDependencies(ScopeType.COMPILE, ScopeType.RUNTIME, ScopeType.TEST).resolve().withTransitivity().asFile();
        war = war.addAsLibraries(libs);

        System.out.println(war.toString(true)); 
        
        return war;
    }
}
/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.dao;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.unitils.UnitilsJUnit4;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

//import org.h2gis.h2spatial.CreateSpatialExtension;

@Slf4j
public abstract class BaseReportingDAOTest extends UnitilsJUnit4 {

    /*protected static final Operation DELETE_ALL = sequenceOf(
            deleteAllFrom("reporting.filter"),
            deleteAllFrom("reporting.report")
            );

    protected static final Operation INSERT_REFERENCE_DATA =
            sequenceOf();

    protected static DbSetupTracker dbSetupTracker = new DbSetupTracker();*/

    private final String TEST_DB_USER = "sa";
    private final String TEST_DB_PASSWORD = "";
    private final String TEST_DB_URL = "jdbc:h2:mem:testdb;LOCK_MODE=0;INIT=CREATE SCHEMA IF NOT EXISTS "+ getSchema() +";DATABASE_TO_UPPER=false;TRACE_LEVEL_SYSTEM_OUT=0";

    /*protected DataSource ds = JdbcConnectionPool.create(TEST_DB_URL, TEST_DB_USER, TEST_DB_PASSWORD);*/

    protected EntityManager em;

    @SneakyThrows
    public BaseReportingDAOTest() {

        //CreateSpatialExtension.initSpatialExtension(ds.getConnection());

        log.info("BuildingEntityManager for unit tests");
        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory(getPersistenceUnitName());
        em = emFactory.createEntityManager();
    }


    protected String getSchema() {
        return "reporting";
    }


    protected String getPersistenceUnitName() {
        return "testPU";
    }

}
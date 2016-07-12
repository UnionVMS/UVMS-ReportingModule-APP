/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.dao;

import com.ninja_squad.dbsetup.operation.Operation;
import eu.europa.ec.fisheries.uvms.BaseDAOTest;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;

public abstract class BaseReportingDAOTest extends BaseDAOTest {

    protected static final Operation DELETE_ALL = sequenceOf(
            deleteAllFrom("reporting.filter"),
            deleteAllFrom("reporting.report")
            );

    protected static final Operation INSERT_REFERENCE_DATA =
            sequenceOf();

    @Override
    protected String getSchema() {
        return "reporting";
    }

    @Override
    protected String getPersistenceUnitName() {
        return "testPU";
    }

}
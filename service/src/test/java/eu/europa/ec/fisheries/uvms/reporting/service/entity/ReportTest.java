/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.entity;

import eu.europa.ec.fisheries.uvms.reporting.entities.Report;
import org.junit.Ignore;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.MockUnitils;
import org.unitils.mock.PartialMock;
import org.unitils.mock.annotation.Dummy;

public class ReportTest extends UnitilsJUnit4 {

    @TestedObject
    private PartialMock<Report> report;

    @Dummy
    private Report dummy;

    @Test
    @Ignore("TODO")
    public void testMerge(){

        report.getMock().merge(dummy);

        report.assertInvoked().mergeDetails(null);
        report.assertInvoked().setIsDeleted(null);
        report.assertInvoked().setDeletedBy(null);
        report.assertInvoked().setVisibility(null);

        MockUnitils.assertNoMoreInvocations();

    }
}
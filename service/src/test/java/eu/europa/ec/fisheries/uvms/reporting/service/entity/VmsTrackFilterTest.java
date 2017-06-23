/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.entity;

import eu.europa.ec.fisheries.uvms.domain.Range;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.DurationRange;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.VmsTrackFilter;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

import static org.junit.Assert.assertEquals;

public class VmsTrackFilterTest extends UnitilsJUnit4 {

    @TestedObject
    private VmsTrackFilter filter = VmsTrackFilter.builder().build();

    @Test
    public void testMerge(){

        VmsTrackFilter incoming = VmsTrackFilter.builder()
                .timeRange(new Range(10F, 20F))
                .durationRange(new DurationRange(200F, 100F))
                .build();

        filter.merge(incoming);

        assertEquals(incoming, filter);
    }
}
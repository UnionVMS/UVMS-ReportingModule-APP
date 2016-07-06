/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.message;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementQuery;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ExtMovementMessageMapper;
import lombok.SneakyThrows;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.junit.Test;

import java.net.URL;

import static junit.framework.Assert.assertTrue;

public class ExtMovementMessageMapperTest {

    @Test
    @SneakyThrows
    public void testMapToGetMovementMapByQueryRequest() {

        URL url = Resources.getResource("ExtendedMovementMessageMapperTests.getMovementMapByQueryRequest.xml");
        String expected = Resources.toString(url, Charsets.UTF_8);

        Diff diff = new Diff(expected, ExtMovementMessageMapper.mapToGetMovementMapByQueryRequest(new MovementQuery()));
        DetailedDiff detailedDiff = new DetailedDiff(diff);
        System.out.println(detailedDiff.getAllDifferences());
        assertTrue(diff.identical());

    }

    @Test(expected = IllegalArgumentException.class)
    @SneakyThrows
    public void testMapToGetMovementMapByQueryRequestException() {
        ExtMovementMessageMapper.mapToGetMovementMapByQueryRequest(null);
    }

    @Test(expected = IllegalArgumentException.class)
    @SneakyThrows
    public void testMapToMovementMapResponseException() {
        ExtMovementMessageMapper.mapToMovementMapResponse(null);
    }
}
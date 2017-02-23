/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.entity;

import eu.europa.ec.fisheries.uvms.reporting.entities.AssetGroupFilter;
import eu.europa.ec.fisheries.wsdl.asset.group.AssetGroup;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AssetGroupFilterTest extends UnitilsJUnit4 {

    @TestedObject
    private AssetGroupFilter filter;

    @Before
    public void before(){
        filter = new AssetGroupFilter();
    }

    @Test
    public void testAssetGroupCriteria(){

        filter = AssetGroupFilter.builder().groupId("5").name("GP5").userName("test").build();

        List<AssetGroup> assetGroups = filter.assetGroupCriteria();

        assertEquals(1, assetGroups.size());
        assertEquals(assetGroups.get(0).getName(), filter.getName());
        assertEquals(assetGroups.get(0).getUser(), filter.getUserName());
        assertEquals(assetGroups.get(0).getGuid(), filter.getGuid());
    }

    @Test
    public void testMerge(){

       filter = AssetGroupFilter.builder().groupId("2").name("GP2").build();

        AssetGroupFilter incoming = AssetGroupFilter.builder().groupId("1").name("GP1").build();

        assertNotEquals(filter, incoming);

        filter.merge(incoming);

        assertEquals(filter, incoming);

    }

}
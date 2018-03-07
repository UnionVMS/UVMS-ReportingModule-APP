/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.message;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;

import javax.jms.TextMessage;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import eu.europa.ec.fisheries.uvms.reporting.message.mapper.ExtAssetMessageMapper;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetHistoryId;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetId;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListQuery;
import junit.framework.Assert;
import lombok.SneakyThrows;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;

public class ExtAssetMessageMapperTest extends UnitilsJUnit4 {

    @Test
    @SneakyThrows
    public void testMapToGetMovementMapByQueryRequest() {

        URL url = Resources.getResource("ExtendedAssetMessageMapperTest.assetListModuleRequest.xml");
        String expected = Resources.toString(url, Charsets.UTF_8).replaceAll("\r", "");

        assertEquals(expected, ExtAssetMessageMapper.mapToGetAssetListByQueryRequest(new AssetListQuery()));
    }

    @Test(expected = IllegalArgumentException.class)
    @SneakyThrows
    public void testMapToGetMovementMapByQueryRequestException() {
        ExtAssetMessageMapper.mapToGetAssetListByQueryRequest(null);
    }

    @Test(expected = IllegalArgumentException.class)
    @SneakyThrows
    public void testMapToAssetListFromResponseException1() {
        ExtAssetMessageMapper.mapToAssetListFromResponse(null, "test");
    }

    @Test(expected = IllegalArgumentException.class)
    @SneakyThrows
    public void testMapToAssetListFromResponseException2() {
        ExtAssetMessageMapper.mapToAssetListFromResponse(mock(TextMessage.class), null);
    }

    @SneakyThrows
    @Test
    public void testGetAssetMap() {

        Asset asset1 = new Asset();
        AssetId assetId1 = new AssetId();
        assetId1.setGuid("guid1");

        AssetHistoryId assetHistoryId = new AssetHistoryId();
        assetHistoryId.setEventId("1");
        asset1.setEventHistory(assetHistoryId);

        Set<Asset> set = new HashSet<>();
        set.add(asset1);
        set.add(asset1);

        Assert.assertEquals(1, ExtAssetMessageMapper.getAssetMap(set).size());
    }

}
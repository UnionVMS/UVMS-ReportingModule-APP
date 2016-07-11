/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.reporting.message.service.ReportingModuleReceiverBean;
import eu.europa.ec.fisheries.uvms.reporting.message.service.AssetModuleSenderBean;
import eu.europa.ec.fisheries.uvms.reporting.service.mapper.FilterProcessor;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListQuery;
import lombok.SneakyThrows;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;
import org.unitils.mock.MockUnitils;
import org.unitils.mock.PartialMock;
import javax.jms.Destination;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.HashSet;

public class AssetServiceBeanTest extends UnitilsJUnit4 {

    @TestedObject
    private PartialMock<AssetServiceBean> service;

    @InjectIntoByType
    private Mock<AssetModuleSenderBean> assetSender;

    @InjectIntoByType
    private Mock<ReportingModuleReceiverBean> assetReceiver;

    private Mock<FilterProcessor> processor;

    private Mock<Destination> destination;

    private Mock<TextMessage> message;

    @Test
    @SneakyThrows
    public void getAssetMapWithAsset(){

        processor.returns(true).hasAssets();
        processor.returns(new AssetListQuery()).toAssetListQuery();
        assetReceiver.returns(message).getMessage(null, null);
        service.returns(new ArrayList<>()).getAssets(null, null);

        service.getMock().getAssetMap(processor.getMock());

        assetSender.assertInvokedInSequence().sendModuleMessage(null, null);
        assetReceiver.assertInvokedInSequence().getMessage(null, null);

        MockUnitils.assertNoMoreInvocations();

    }

    @Test
    @SneakyThrows
    public void getAssetMapWithAssetsAndAssetGroup(){

        processor.returns(true).hasAssets();
        processor.returns(true).hasAssetGroups();

        processor.returns(new AssetListQuery()).toAssetListQuery();
        processor.returns(new HashSet<>()).getAssetGroupList();

        assetReceiver.returns(message).getMessage(null, null);
        service.returns(new ArrayList<>()).getAssets(null, null);

        service.getMock().getAssetMap(processor.getMock());

        assetSender.assertInvokedInSequence().sendModuleMessage(null, null);
        assetReceiver.assertInvokedInSequence().getMessage(null, null);

        assetSender.assertInvokedInSequence().sendModuleMessage(null, null);
        assetReceiver.assertInvokedInSequence().getMessage(null, null);

        MockUnitils.assertNoMoreInvocations();

    }

}
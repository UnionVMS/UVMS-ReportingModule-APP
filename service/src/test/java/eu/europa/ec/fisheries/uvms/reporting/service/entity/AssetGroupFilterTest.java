package eu.europa.ec.fisheries.uvms.reporting.service.entity;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.AssetGroupFilter;
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

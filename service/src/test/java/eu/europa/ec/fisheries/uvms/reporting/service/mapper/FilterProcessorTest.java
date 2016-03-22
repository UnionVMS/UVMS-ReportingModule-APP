package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.SearchKey;
import eu.europa.ec.fisheries.uvms.exception.ProcessorException;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.AssetFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.AssetGroupFilter;
import eu.europa.ec.fisheries.wsdl.asset.group.AssetGroup;
import eu.europa.ec.fisheries.wsdl.asset.types.ConfigSearchField;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListCriteriaPair;
import lombok.SneakyThrows;
import org.joda.time.DateTime;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;

public class FilterProcessorTest extends UnitilsJUnit4 {

    @TestedObject
    private FilterProcessor processor;

    @Test
    @SneakyThrows
    public void testInitWithAssetFilter() {

        Set<Filter> filterList = new HashSet<>();

        AssetFilter assetFilter = AssetFilter.builder().guid("guid").build();
        filterList.add(assetFilter);

        processor = new FilterProcessor(filterList, new DateTime());

        assertEquals(1, processor.getAssetListCriteriaPairs().size());
        assertEquals(0, processor.getRangeCriteria().size());
        assertEquals(0, processor.getAssetGroupList().size());
        assertEquals(1, processor.getMovementListCriteria().size());

        List<AssetListCriteriaPair> assetListCriteriaPairList = new ArrayList<>();
        assetListCriteriaPairList.addAll(processor.getAssetListCriteriaPairs());

        List<ListCriteria> listCriteria = new ArrayList<>();
        listCriteria.addAll(processor.getMovementListCriteria());

        processor.getAssetListCriteriaPairs();
        assertEquals(assetListCriteriaPairList.get(0).getKey(), ConfigSearchField.GUID);
        assertEquals(assetListCriteriaPairList.get(0).getValue(), "guid");

        assertEquals(listCriteria.get(0).getKey(), SearchKey.CONNECT_ID);
        assertEquals(listCriteria.get(0).getValue(), "guid");

    }

    @Test
    @SneakyThrows
    public void testInitWithAssetGroupFilter() {

        Set<Filter> filterList = new HashSet<>();

        AssetGroupFilter assetGroupFilter = new AssetGroupFilter();
        assetGroupFilter.setGuid("1");
        assetGroupFilter.setUserName("test");

        filterList.add(assetGroupFilter);

        processor = new FilterProcessor(filterList, new DateTime());

        List<AssetGroup> assetGroupList = new ArrayList<>();
        assetGroupList.addAll(processor.getAssetGroupList());

        assertEquals(0, processor.getAssetListCriteriaPairs().size());
        assertEquals(0, processor.getRangeCriteria().size());
        assertEquals(1, processor.getAssetGroupList().size());
        assertEquals(0, processor.getMovementListCriteria().size());

        assertEquals(assetGroupList.get(0).getGuid(), "1");
        assertEquals(assetGroupList.get(0).getName(), null);
        assertEquals(assetGroupList.get(0).getUser(), "test");

    }

    @Test(expected = ProcessorException.class)
    @SneakyThrows
    public void shouldThrowExceptionWhenEmptyFilterList() {
        Set<Filter> filterList = new HashSet<>();
        processor = new FilterProcessor(filterList, new DateTime());
    }

    @Test(expected = ProcessorException.class)
    @SneakyThrows
    public void testSanityFilterSetNull() {
        processor = new FilterProcessor(null, new DateTime());
    }

}

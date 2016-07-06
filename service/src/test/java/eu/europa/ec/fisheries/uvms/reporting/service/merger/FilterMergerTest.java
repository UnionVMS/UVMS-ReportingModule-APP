/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.merger;

import eu.europa.ec.fisheries.schema.movement.v1.MovementActivityTypeType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.FilterDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.ReportDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.AreaFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.CommonFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.FilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.AssetFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.AssetGroupFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.VmsPositionFilterDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.AreaFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Filter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Selector;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.AssetFilter;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.reporting.service.dto.CommonFilterDTO.CommonFilterDTOBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.PositionSelectorDTO.PositionSelectorDTOBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.AssetFilterDTO.AssetFilterDTOBuilder;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.AssetGroupFilterDTO.AssetGroupFilterDTOBuilder;
import static junit.framework.TestCase.assertTrue;
import static org.unitils.mock.MockUnitils.assertNoMoreInvocations;

public class FilterMergerTest extends UnitilsJUnit4 {

    @PersistenceContext
    private EntityManager em;

    @TestedObject
    private FilterMerger merger = new FilterMerger(em);

    @InjectIntoByType
    private Mock<ReportDAO> reportDAOMock;

    @InjectIntoByType
    private Mock<FilterDAO> filterDAOMock;

    private AssetFilterDTO asset1;
    private AssetGroupFilterDTO vgroup2;
    private AssetFilterDTO asset2;
    private AssetGroupFilterDTO vgroup1;
    private VmsPositionFilterDTO vms;
    private CommonFilterDTO common;
    private AreaFilterDTO area;

    @Before
    public void before(){

        asset1 = AssetFilterDTOBuilder().id(47L).guid("guid1").name("asset1").build();

        vgroup2 = AssetGroupFilterDTOBuilder().id(48L).guid("2").userName("ddd").name("name").build();

        asset2 = AssetFilterDTOBuilder().id(49L).guid("gui4564").name("asset2").build();

        vgroup1 = AssetGroupFilterDTOBuilder().id(45L).guid("1").userName("ffsdfs").name("name2").build();

        vms = VmsPositionFilterDTO.VmsPositionFilterDTOBuilder().id(1L)
                .movementActivity(MovementActivityTypeType.ANC)
                .minimumSpeed(100F)
                .maximumSpeed(123F)
                .movementType(MovementTypeType.ENT)
                .build();

        common = CommonFilterDTOBuilder()
                .id(46L)
                .positionSelector(PositionSelectorDTOBuilder().selector(Selector.all).build())
                .endDate(DateUtils.stringToDate("2015-10-09 08:56:48 +0200"))
                .startDate(DateUtils.stringToDate("2015-10-09 08:56:48 +0200"))
                .build();

        area = AreaFilterDTO.AreaFilterDTOBuilder().areaId(20L).areaType("EEZ").build();
    }


    @Test
    @SneakyThrows
    public void testMergeAssetFilterUntouched(){

        Collection<FilterDTO> collection =  new ArrayList<>();

        collection.add(asset1);

        List<Filter> existingFilters = new ArrayList<>();
        AssetFilter existingFilter = AssetFilter.builder().guid("guid1").name("asset1").build();
        existingFilters.add(existingFilter);

        filterDAOMock.returns(existingFilters).listByReportId(null);

        boolean updated = merger.merge(collection);

        assertNoMoreInvocations();

        assertTrue(!updated);

    }


    @Test
    @SneakyThrows
    public void testMergeAssetFilter(){

        asset1 = AssetFilterDTOBuilder().id(null).guid("ae9a03a4-62c6-462e-a5ab-27c22439b7e6").name("EMMALIE").build();
        asset2 = AssetFilterDTOBuilder().id(null).guid("sf3da03a2-13c2-342e-v3ab-14c12469b7e").name("JEANNE").build();

        Collection<FilterDTO> incoming =  new ArrayList<>();
        incoming.add(asset1);
        incoming.add(asset2);

        List<Filter> existingFilters = new ArrayList<>();
        AssetFilter existingFilter = AssetFilter.builder().guid("ae9a03a4-62c6-462e-a5ab-27c22439b7e6").name("EMMALIE").build();

        existingFilters.add(existingFilter);

        filterDAOMock.returns(existingFilters).listByReportId(null);

        boolean updated = merger.merge(incoming);

        filterDAOMock.assertInvoked().createEntity(null);
        assertNoMoreInvocations();
        //assertTrue(updated);

    }

    @Test
    @SneakyThrows
    public void testMergeAssetFilterUpdateAndDelete(){

        Collection<FilterDTO> collection =  new ArrayList<>();
        collection.add(asset1);

        List<Filter> existingFilters = new ArrayList<>();
        AssetFilter existingFilter = AssetFilter.builder().guid("guid").name("asset1").build();
        AssetFilter existingFilter2 = AssetFilter.builder().guid("dddd").name("vvvv").build();

        existingFilters.add(existingFilter); // this one is an update
        existingFilters.add(existingFilter2); // this one has to go

        filterDAOMock.returns(existingFilters).listByReportId(null);

        boolean updated = merger.merge(collection);

        filterDAOMock.assertInvoked().createEntity(null);
       // filterDAOMock.assertInvoked().deleteBy(49L);
       // filterDAOMock.assertInvoked().deleteBy(47L);
       // assertNoMoreInvocations();
        assertTrue(updated);
    }

    @Test
    @SneakyThrows
    public void testMergeAssetFilterUpdateAndDeleteAreaFilters(){

        Collection<FilterDTO> collection =  new ArrayList<>();
        collection.add(area);

        List<Filter> existingFilters = new ArrayList<>();
        AreaFilter existingFilter = AreaFilter.builder().areaId(10L).areaType("BBB").build();
        AreaFilter existingFilter2 = AreaFilter.builder().areaId(11L).areaType("CCC").build();

        existingFilters.add(existingFilter); // this one is an update
        existingFilters.add(existingFilter2); // this one has to go

        filterDAOMock.returns(existingFilters).listByReportId(null);

        boolean updated = merger.merge(collection);

        filterDAOMock.assertInvoked().createEntity(null);
       // filterDAOMock.assertInvoked().deleteBy(47L);
       // filterDAOMock.assertInvoked().deleteBy(48L);
        //assertNoMoreInvocations();
        assertTrue(updated);
    }

    @Test
    @SneakyThrows
    public void testMergeAssetFilterInsert(){

        Collection<FilterDTO> collection =  new ArrayList<>();
        collection.add(asset1);

        Report report = Report.builder().build();
        filterDAOMock.returns(new ArrayList<>()).listByReportId(null); // empty array
        reportDAOMock.returns(report).findEntityById(Report.class, null);
        boolean updated = merger.merge(collection);

        filterDAOMock.assertNotInvoked().updateEntity(null);
        filterDAOMock.assertNotInvoked().deleteEntity(null, null);
        AssetFilter assetFilter = AssetFilter.builder().build();
        filterDAOMock.assertInvoked().createEntity(assetFilter);
        assertNoMoreInvocations();
        assertTrue(updated);
    }

}
/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.merger;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import eu.europa.ec.fisheries.schema.movement.v1.MovementActivityTypeType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.BaseReportingDAOTest;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.FilterDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.ReportDAO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.*;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.*;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static eu.europa.ec.fisheries.uvms.reporting.service.dto.AssetFilterDTO.AssetFilterDTOBuilder;
import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.unitils.mock.MockUnitils.assertNoMoreInvocations;

public class FilterMergerTest extends BaseReportingDAOTest {

    private FilterDAO filterDAO = new FilterDAO(em);

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

        Operation operation =
                sequenceOf(
                        DELETE_ALL,
                        INSERT_REFERENCE_DATA,
                        insertInto("reporting.report")
                                .columns("ID", ReportDetails.CREATED_BY, ReportDetails.NAME, Audit.CREATED_ON, ReportDetails.WITH_MAP, Report.VISIBILITY, "is_deleted", ReportDetails.SCOPE_NAME)
                                .values(1, "testUser", "France", java.sql.Date.valueOf("2014-12-12"), '1', VisibilityEnum.PRIVATE, 'N', "testScope")
                                .values(2, "testUser", "United States", java.sql.Date.valueOf("2014-12-13"), '1', VisibilityEnum.PRIVATE, 'N', "testScope")
                                .build(),
                        insertInto("reporting.filter")
                                .columns("filter_id", "guid", "name", Filter.REPORT_ID, "filter_type")
                                .values(49L, "guid1", "asset1", 1L, "ASSET")
                                .values(47L, "dddd", "vvvv", 1L, "ASSET")
                                .values(50L, "ae9a03a4-62c6-462e-a5ab-27c22439b7e6", "EMMALIE", 1L, "ASSET")
                                .build(),
                        insertInto("reporting.filter")
                                .columns("filter_id", "min_speed", "max_speed", "MOV_ACTIVITY", "MOV_TYPE", "filter_type", Filter.REPORT_ID)
                                .values(1L, "2", "2", MovementActivityTypeType.ANC.ordinal(), MovementTypeType.ENT.ordinal(), "VMSPOS", 2L)
                                .build());


        DbSetup dbSetup = new DbSetup(new DataSourceDestination(ds), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);

        asset1 = AssetFilterDTOBuilder().id(47L).guid("guid1").name("asset1").build();

        //vgroup2 = AssetGroupFilterDTOBuilder().id(48L).guid("2").userName("ddd").name("name").build();

        //vasset2 = AssetFilterDTOBuilder().id(49L).guid("gui4564").name("asset2").build();

        //vvgroup1 = AssetGroupFilterDTOBuilder().id(45L).guid("1").userName("ffsdfs").name("name2").build();

        //vcommon = CommonFilterDTOBuilder()
        //v.id(46L)
        //v.positionSelector(PositionSelectorDTOBuilder().selector(Selector.all).build())
        //v.endDate(DateUtils.stringToDate("2015-10-09 08:56:48 +0200"))
        //v.startDate(DateUtils.stringToDate("2015-10-09 08:56:48 +0200"))
        //v.build();

        //varea = AreaFilterDTO.AreaFilterDTOBuilder().areaId(20L).areaType("EEZ").build();
    }

    @Test
    @SneakyThrows
    public void testUpdateWithVmsFilter(){

        Collection<FilterDTO> collection =  new ArrayList<>();
        VmsPositionFilterDTO positionFilterDTO = VmsPositionFilterDTO.VmsPositionFilterDTOBuilder().id(1L)
                .movementActivity(MovementActivityTypeType.AUT)
                .minimumSpeed(100F).maximumSpeed(123F)
                .movementType(MovementTypeType.EXI).build();
        collection.add(positionFilterDTO);

        List<Filter> existingFilters = new ArrayList<>();
        existingFilters.add(filterDAO.findEntityById(Filter.class, 1L));

        filterDAOMock.returns(existingFilters).listByReportId(null);

        boolean updated = merger.merge(collection);

        filterDAOMock.assertInvoked().updateEntity(null);

        assertNoMoreInvocations();

        assertTrue(updated);

        FilterDTO accept = filterDAO.findEntityById(Filter.class, 1L).accept(new Filter.FilterToDTOVisitor());

        assertEquals(accept, positionFilterDTO);

    }


    @Test
    @SneakyThrows
    public void testMergeAssetFilterUntouched(){

        Collection<FilterDTO> collection =  new ArrayList<>();
        collection.add(AssetFilterDTOBuilder().id(47L).guid("guid1").name("asset1").build());

        List<Filter> existingFilters = new ArrayList<>();
        existingFilters.add(filterDAO.findEntityById(Filter.class, 49L));

        filterDAOMock.returns(existingFilters).listByReportId(null);

        boolean updated = merger.merge(collection);

        assertNoMoreInvocations();

        assertTrue(!updated);

    }


    @Test
    @SneakyThrows
    public void testMergeAssetFilter(){

        asset1 = AssetFilterDTOBuilder().id(50L).guid("ae9a03a4-62c6-462e-a5ab-27c22439b7e6").name("EMMALIE").build();
        asset2 = AssetFilterDTOBuilder().id(null).guid("sf3da03a2-13c2-342e-v3ab-14c12469b7e").name("JEANNE").build();

        Collection<FilterDTO> incoming =  new ArrayList<>();
        incoming.add(asset1);
        incoming.add(asset2);

        List<Filter> existingFilters = new ArrayList<>();
        existingFilters.add(filterDAO.findEntityById(Filter.class, 50L));

        filterDAOMock.returns(existingFilters).listByReportId(null);

        merger.merge(incoming);

        filterDAOMock.assertInvoked().createEntity(null);
        assertNoMoreInvocations();

    }

    @Test
    @SneakyThrows
    public void testMergeAssetFilterUpdateAndDelete(){

        Collection<FilterDTO> collection =  new ArrayList<>();
        collection.add(AssetFilterDTOBuilder().guid("guidguid").name("asset1").build());

        List<Filter> existingFilters = new ArrayList<>();

        existingFilters.add(filterDAO.findEntityById(Filter.class, 49L));
        existingFilters.add(filterDAO.findEntityById(Filter.class, 47L));

        filterDAOMock.returns(existingFilters).listByReportId(null);

        boolean updated = merger.merge(collection);

        filterDAOMock.assertInvoked().createEntity(null);
        filterDAOMock.assertInvoked().deleteBy(47L);
        filterDAOMock.assertInvoked().deleteBy(49L);
        assertNoMoreInvocations();
        assertTrue(updated);
    }

    @Test
    @SneakyThrows
    public void testMergeAssetFilterInsert(){

        Collection<FilterDTO> collection =  new ArrayList<>();
        collection.add(AssetFilterDTOBuilder().id(47L).guid("guid1").name("asset1").build());

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
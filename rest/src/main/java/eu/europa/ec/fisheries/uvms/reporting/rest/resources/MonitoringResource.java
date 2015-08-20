package eu.europa.ec.fisheries.uvms.reporting.rest.resources;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.schema.movement.v1.MovementBaseType;
import eu.europa.ec.fisheries.uvms.reporting.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.reporting.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.reporting.rest.mapper.AssetMapper;
import eu.europa.ec.fisheries.uvms.reporting.rest.mapper.MovementBaseTypeMapper;
import eu.europa.ec.fisheries.uvms.reporting.rest.mapper.MovementPointMapper;
import eu.europa.ec.fisheries.uvms.reporting.rest.temp.MockMovementData;
import eu.europa.ec.fisheries.uvms.reporting.rest.temp.MockVesselData;
import eu.europa.ec.fisheries.uvms.reporting.rest.temp.MockingUtils;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.MonitoringService;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MonitoringDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MovementDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.SegmentDto;
import eu.europa.ec.fisheries.uvms.rest.FeatureToGeoJson;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import org.geotools.data.DataUtilities;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.GeometryBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.json.simple.JSONObject;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * //TODO create test
 */
@Path("/monitoring")
public class MonitoringResource {

    final static Logger LOG = LoggerFactory.getLogger(MonitoringResource.class);

    @EJB
    MonitoringService monitoringService;

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/movement")
    @SuppressWarnings("unchecked")
    public ResponseDto getMonitoringData() {
        MonitoringDto monitoringDTO;

        LOG.info("Getting movement data...");

        HashSet hashSet = new HashSet();
        hashSet.add(1); hashSet.add(3); hashSet.add(3);
        monitoringDTO = monitoringService.getMonitoringData(hashSet);

        return new ResponseDto(monitoringDTO, ResponseCode.OK);
    }

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/movement/mockdata")
    @SuppressWarnings("unchecked")
    public ResponseDto getMockData() {
        try {
            LOG.info("Getting movement data...");

            int totalVessels = 1;
            int totalMovements = 10;

            List<MovementBaseType> movementBaseTypeList = MockMovementData.getDtoList(totalMovements);
            List<Vessel> assetList = MockVesselData.getVesselDtoList(totalVessels);
            List<MovementDto> movementDtos = new ArrayList<>();

            for (int i=0; i < movementBaseTypeList.size(); i++){
                MovementDto movementDto = MovementBaseTypeMapper.INSTANCE.movementBaseTypeToMovementDto(movementBaseTypeList.get(MockingUtils.randInt(0, totalMovements - 1)));
                movementDto.setId(String.valueOf(MockingUtils.randInt(0, totalMovements)));
                movementDto.setAsset(AssetMapper.INSTANCE.vesselToAssetDto(assetList.get(MockingUtils.randInt(0, totalVessels - 1))));
                movementDtos.add(movementDto);
            }

            List<SegmentDto> segmentDtos = new ArrayList<>();

            for (int i=0; i < 100; i++){
                SegmentDto segmentDto = new SegmentDto();
                segmentDto.setPresentPosition(MovementPointMapper.INSTANCE.movementPointToGeometry(MockMovementData.getMovementPoint()));
                segmentDto.setPreviousPosition(MovementPointMapper.INSTANCE.movementPointToGeometry(MockMovementData.getMovementPoint()));
                segmentDtos.add(segmentDto);
            }

            MonitoringDto monitoringDTO = new MonitoringDto();
          //  monitoringDTO.setMovements(movementDtos);
          //  monitoringDTO.setSegments(segmentDtos);

            return new ResponseDto(monitoringDTO, ResponseCode.OK);

        } catch (Exception e) {
            LOG.error("[ Error when getting vessel list. ] ", e);
            return null; //ErrorHandler.getFault(e);
        }
    }

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/movement/mockdata2")
    @SuppressWarnings("unchecked")
    public ResponseDto getMockData2() {
        int totalMovements = 10;
        int totalVessels = 1;

        List<MovementBaseType> movementBaseTypeList = MockMovementData.getDtoList(totalMovements);

        List<Vessel> assetList = MockVesselData.getVesselDtoList(totalVessels);
        List<MovementDto> movementDtos = new ArrayList<>();

        for (int i=0; i < movementBaseTypeList.size(); i++){
            MovementDto movementDto = MovementBaseTypeMapper.INSTANCE.movementBaseTypeToMovementDto(movementBaseTypeList.get(MockingUtils.randInt(0, totalMovements - 1)));
            movementDto.setId(String.valueOf(MockingUtils.randInt(0, totalMovements)));
            movementDto.setAsset(AssetMapper.INSTANCE.vesselToAssetDto(assetList.get(MockingUtils.randInt(0, totalVessels - 1))));
            movementDtos.add(movementDto);
        }

        return new ResponseDto(movementDtos.get(0), ResponseCode.OK);
    }

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/movement/mockdata3")
    @SuppressWarnings("unchecked")
    public ResponseDto getMockData3() throws SchemaException {

        int totalMovements = 10;
        int totalVessels = 1;

        List<MovementBaseType> movementBaseTypeList = MockMovementData.getDtoList(totalMovements);

        List<Vessel> assetList = MockVesselData.getVesselDtoList(totalVessels);
        List<MovementDto> movementDtos = new ArrayList<>();

        for (int i=0; i < movementBaseTypeList.size(); i++){
            MovementDto movementDto = MovementBaseTypeMapper.INSTANCE.movementBaseTypeToMovementDto(movementBaseTypeList.get(MockingUtils.randInt(0, totalMovements - 1)));
            movementDto.setId(String.valueOf(MockingUtils.randInt(0, totalMovements)));
            movementDto.setAsset(AssetMapper.INSTANCE.vesselToAssetDto(assetList.get(MockingUtils.randInt(0, totalVessels - 1))));
            movementDtos.add(movementDto);
        }

        MonitoringDto monitoringDto = new MonitoringDto();

        // build feature
        SimpleFeatureType TYPE = DataUtilities.createType("Location", "geometry:Point:srid=4326,name:String,country:String");
        DefaultFeatureCollection features = new DefaultFeatureCollection(null, TYPE);
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory(null);
        featureBuilder.add(gf.createPoint(new Coordinate(12.5000, 41.8833)));
        featureBuilder.set("name", "New York");
        featureBuilder.set("country", "USA");
        features.add(featureBuilder.buildFeature("1"));

        return new ResponseDto(new FeatureToGeoJson().convert(features), ResponseCode.OK);
    }
}

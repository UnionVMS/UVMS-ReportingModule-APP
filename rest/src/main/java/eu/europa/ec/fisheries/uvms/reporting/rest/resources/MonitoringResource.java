package eu.europa.ec.fisheries.uvms.reporting.rest.resources;

import eu.europa.ec.fisheries.schema.movement.v1.MovementBaseType;
import eu.europa.ec.fisheries.uvms.reporting.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.reporting.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.reporting.rest.mapper.AssetMapper;
import eu.europa.ec.fisheries.uvms.reporting.rest.mapper.MovementBaseTypeMapper;
import eu.europa.ec.fisheries.uvms.reporting.rest.temp.MockMovementData;
import eu.europa.ec.fisheries.uvms.reporting.rest.temp.MockVesselData;
import eu.europa.ec.fisheries.uvms.reporting.rest.temp.MockingUtils;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.MonitoringService;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MonitoringDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MovementDto;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.*;

/**
 * //TODO create test
 */
@Path("/monitoring")
@Stateless
public class MonitoringResource {

    final static Logger LOG = LoggerFactory.getLogger(MonitoringResource.class);

    @Inject
    MonitoringService monitoringService;

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/movement")
    @SuppressWarnings("unchecked")
    public ResponseDto getData() {
        MonitoringDto monitoringDTO;

        LOG.info("Getting movement data...");

        HashSet hashSet = new HashSet();
        hashSet.add(1); hashSet.add(3); hashSet.add(3);
        monitoringDTO = monitoringService.getMovements(hashSet);

        return new ResponseDto(monitoringDTO, ResponseCode.OK);
    }

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/movement/mockdata")
    @SuppressWarnings("unchecked")
    public ResponseDto getMockData() {
        try {
            LOG.info("Getting movement data...");

            List<MovementBaseType> movementBaseTypeList = MockMovementData.getDtoList(100);
            List<Vessel> assetList = MockVesselData.getVesselDtoList(20);
            int index = 0;
            List<MovementDto> movementDtos = new ArrayList<>();

            for (int i=0;i<movementBaseTypeList.size();i++){
                MovementDto movementDto = MovementBaseTypeMapper.INSTANCE.movementBaseTypeToMovementDto(movementBaseTypeList.get(MockingUtils.randInt(0, 9)));
                movementDto.setId(String.valueOf(MockingUtils.randInt(0, 100)));
                movementDto.setAsset(AssetMapper.INSTANCE.vesselToAssetDto(assetList.get(index)));
                if(i%(movementBaseTypeList.size()/5)==0) index++;
                movementDtos.add(movementDto);
            }

            MonitoringDto monitoringDTO = new MonitoringDto();
            monitoringDTO.setMovements(movementDtos);

            return new ResponseDto(monitoringDTO, ResponseCode.OK);

        } catch (Exception e) {
            LOG.error("[ Error when getting vessel list. ] ", e);
            return null; //ErrorHandler.getFault(e);
        }
    }
}

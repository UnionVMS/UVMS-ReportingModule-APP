/*
 *
 * Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingTripIdWithGeometry;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.VesselIdentifierSchemeIdEnum;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.VesselIdentifierType;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.TripDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by padhyad on 3/22/2017.
 */
@Slf4j
public class FishingTripMapperTest {

    @Test
    public void testFishingTripMapperForTripDto() throws Exception {

        //Setup data
        FishingTripIdWithGeometry fishingTripIdWithGeometry = new FishingTripIdWithGeometry();
        fishingTripIdWithGeometry.setFirstFishingActivity("First Activity");
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(new Date());
        XMLGregorianCalendar firstActivityDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
        fishingTripIdWithGeometry.setFirstFishingActivityDateTime(firstActivityDate);
        fishingTripIdWithGeometry.setFlagState("BEL");
        fishingTripIdWithGeometry.setGeometry("MULTIPOINT(53.8 -4.3167)");
        fishingTripIdWithGeometry.setLastFishingActivity("Last Activity");
        XMLGregorianCalendar lastActivityDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
        fishingTripIdWithGeometry.setLastFishingActivityDateTime(lastActivityDate);
        fishingTripIdWithGeometry.setNoOfCorrections(10);
        XMLGregorianCalendar relativeFirstDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
        fishingTripIdWithGeometry.setRelativeFirstFaDateTime(relativeFirstDate);
        XMLGregorianCalendar relativeLastDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
        fishingTripIdWithGeometry.setRelativeLastFaDateTime(relativeLastDate);
        fishingTripIdWithGeometry.setSchemeId("SchemeId");
        fishingTripIdWithGeometry.setTripDuration(20.0);
        fishingTripIdWithGeometry.setTripId("TRP_01");
        List<VesselIdentifierType> vesselIdentifierTypeList = new ArrayList<>();
        vesselIdentifierTypeList.add(new VesselIdentifierType(VesselIdentifierSchemeIdEnum.CFR, "12345"));
        fishingTripIdWithGeometry.setVesselIdLists(vesselIdentifierTypeList);

        fishingTripIdWithGeometry.setFirstFishingActivityDateTime(firstActivityDate);

        TripDTO tripDTO = FishingTripMapper.INSTANCE.fishingTripToTripDto(fishingTripIdWithGeometry);

        assertEquals(tripDTO.getFirstFishingActivity(), fishingTripIdWithGeometry.getFirstFishingActivity());
        assertEquals(tripDTO.getFirstFishingActivityDateTime(), gc.getTime());
        assertEquals(tripDTO.getFlagState(), fishingTripIdWithGeometry.getFlagState());
        assertEquals(tripDTO.getGeometry(), fishingTripIdWithGeometry.getGeometry());
        assertEquals(tripDTO.getLastFishingActivity(), fishingTripIdWithGeometry.getLastFishingActivity());
        assertEquals(tripDTO.getLastFishingActivityDateTime(), gc.getTime());
        assertEquals(tripDTO.getNoOfCorrections().intValue(), fishingTripIdWithGeometry.getNoOfCorrections());
        assertEquals(tripDTO.getSchemeId(), fishingTripIdWithGeometry.getSchemeId());
        assertEquals(tripDTO.getTripId(), fishingTripIdWithGeometry.getTripId());
        printDtoOnConsole(tripDTO, TripDTO.class);
    }

    private void printDtoOnConsole(Object tripDTO, final Class<?> view) throws JsonProcessingException {
        System.out.println(getObjectMapperForView(view).configure(SerializationFeature.INDENT_OUTPUT, true).writeValueAsString(tripDTO));
    }

    private ObjectMapper getObjectMapperForView(final Class<?> view) {
        return new ObjectMapper() {
            private static final long serialVersionUID = 1L;
            @Override
            protected DefaultSerializerProvider _serializerProvider(SerializationConfig config) {
                return super._serializerProvider(config.withView(view));
            }
        };
    }
}

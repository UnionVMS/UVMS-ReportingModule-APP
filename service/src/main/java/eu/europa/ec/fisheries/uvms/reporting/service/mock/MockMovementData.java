package eu.europa.ec.fisheries.uvms.reporting.service.mock;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTWriter;
import eu.europa.ec.fisheries.schema.movement.mobileterminal.v1.MobileTerminalId;
import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.ListPagination;
import eu.europa.ec.fisheries.schema.movement.search.v1.SearchKey;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSourceType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.uvms.common.MockingUtils;
import org.apache.commons.lang.RandomStringUtils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

public class MockMovementData {

    static WKTWriter writer = new WKTWriter();
    static int counter = 0;
    public static MovementType getDto(Point point) {
        MovementType dto = new MovementType();
        dto.setConnectId(String.valueOf(++counter));
        dto.setConnectId(UUID.randomUUID().toString());
        dto.setCalculatedSpeed(BigDecimal.valueOf(MockingUtils.randInt(0, 50)));
        dto.setCourse(MockingUtils.randInt(1, 20));
        dto.setMeasuredSpeed(BigDecimal.valueOf(MockingUtils.randInt(0, 50)));
        dto.setMovementType(MovementTypeType.ENT);
        dto.setMobileTerminal(getMobTermId());
        dto.setSource(MovementSourceType.INMARSAT_C);
        dto.setStatus(RandomStringUtils.randomAlphabetic(MockingUtils.randInt(5, 20)));
        dto.setPositionTime(getPositionTime());
        dto.setWkt(writer.write(point));
        return dto;
    }

    public static MobileTerminalId getMobTermId() {
        MobileTerminalId id = new MobileTerminalId();
        id.setId("ABC-80+");
        return id;
    }

    public static ListPagination getListPagination() {
        ListPagination criteria = new ListPagination();
        criteria.setListSize(BigInteger.valueOf(10L));
        criteria.setPage(BigInteger.valueOf(1L));
        return criteria;
    }

    public static ListCriteria getListCtieria() {
        ListCriteria criteria = new ListCriteria();
        criteria.setKey(SearchKey.CONNECT_ID);
        criteria.setValue("value");
        return criteria;
    }

    public static XMLGregorianCalendar getPositionTime() {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
}

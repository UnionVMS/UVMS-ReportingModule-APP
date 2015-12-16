package eu.europa.ec.fisheries.uvms.reporting.service.util;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class GeometryUtil {

    private GeometryUtil(){}

    public static Geometry toGeometry(final String wkt) throws ParseException {

        Geometry geometry = null;

        if (wkt != null) {

            WKTReader wktReader = new WKTReader();
            geometry = wktReader.read(wkt);

        }

        return geometry;
    }

}

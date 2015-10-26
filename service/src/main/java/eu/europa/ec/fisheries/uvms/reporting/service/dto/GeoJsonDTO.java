package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;
import org.opengis.feature.simple.SimpleFeature;

public abstract class GeoJsonDTO {

    protected static final String GEOMETRY = "geometry";

    @JsonIgnore
    protected Geometry geometry;

    protected Geometry toGeometry(final String wkt) throws ReportingServiceException {
        if (wkt != null){
            WKTReader wktReader = new WKTReader();
            try {
                return geometry = wktReader.read(wkt);
            } catch (ParseException e) {
                throw new ReportingServiceException("ERROR WHILE PARSING GEOMETRY", e);
            }
        }
        return null;
    }

    abstract SimpleFeature toFeature() throws ReportingServiceException;

}

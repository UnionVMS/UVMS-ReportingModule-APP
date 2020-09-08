/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.util;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class GeometryUtil {

    private static final GeometryFactory FACTORY = new GeometryFactory();
    static final int SRID = 4326;

    private GeometryUtil() {
        super();
    }

    public static Geometry toGeometry(final String wkt) throws ParseException {
        Geometry geometry = null;
        if (wkt != null) {
            WKTReader wktReader = new WKTReader();
            geometry = wktReader.read(wkt);
        }
        return geometry;
    }

    /**
     * Returns a LineString for the given wkt with the Geometry coordinates
     * @param wkt The wkt String
     * @return LineString object
     * @throws ParseException Thrown if string has errors
     */
    public static LineString toLineString(final String wkt) throws ParseException{
        return getLineString(toGeometry(wkt).getCoordinates());
    }

    /**
     * Returns a LineString for insertion in database
     *
     * @param sequence Coordinate array
     * @return LineString object
     */
    public static LineString getLineString(Coordinate[] sequence) {
        LineString lineString = FACTORY.createLineString(sequence);
        lineString.setSRID(SRID);
        return lineString;
    }
}
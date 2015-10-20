package eu.europa.ec.fisheries.uvms.reporting.service.mock.util;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTWriter;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSegment;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTrack;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.uvms.common.MockingUtils;
import eu.europa.ec.fisheries.uvms.reporting.service.mock.MockMovementData;
import org.geotools.geometry.jts.JTSFactoryFinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MockPointsReader {

    private List<Point> points = new ArrayList<>();
    private List<LineString> linestringList = new ArrayList<>();
    private GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
    public List<MovementSegment> segmentList = new ArrayList<>();
    public List<MovementType> movementTypeList = new ArrayList<>();
    public List<MovementTrack> movementTrackList = new ArrayList<>();

    public MockPointsReader(){
        try {
            readPoints();
            try {
                buildMovementsSegmentAndTracks();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buildMovementsSegmentAndTracks() throws ParseException {

        WKTWriter wktWriter = new WKTWriter();
        GeometryFactory geomFac = new GeometryFactory();

        for (int i=1; i < points.size() -1; i++){
            Point element = points.get(i-1);
            MovementType movementType = MockMovementData.getDto(element);
            movementType.setWkt(wktWriter.write(element));
            movementTypeList.add(movementType);
            if (i%2 == 0){
                Point point = points.get(i-2);
                Point point1 = points.get(i-1);
                Coordinate[] lsc = new Coordinate[2];
                lsc[0] = new Coordinate(point.getX(),point.getY());
                lsc[1] = new Coordinate(point1.getX(),point1.getY());
                LineString lnG = geomFac.createLineString(lsc);
                linestringList.add(lnG);
                String write = wktWriter.write(lnG);
                MovementSegment movementSegment = new MovementSegment();
                movementSegment.setCourseOverGround((double) MockingUtils.randInt(0, 20));
                movementSegment.setDistance((double) MockingUtils.randInt(0, 20));
                movementSegment.setDuration((double) MockingUtils.randInt(0, 20));
                movementSegment.setSpeedOverGround((double) MockingUtils.randInt(0, 20));
                movementSegment.setWkt(write);
                movementSegment.setId(String.valueOf(i));
                segmentList.add(movementSegment);
            }
        }

        // create track
        List<Coordinate> coordinateList = new ArrayList<>();
        for(int i=0; i < linestringList.size(); i++){
            LineString lineString = linestringList.get(i);
            coordinateList.add(lineString.getCoordinate());
            if(coordinateList.size() == 5){
                Coordinate[] lsc = new Coordinate[5];
                lsc[0] = coordinateList.get(0);
                lsc[1] = coordinateList.get(1);
                lsc[2] = coordinateList.get(2);
                lsc[3] = coordinateList.get(3);
                lsc[4] = coordinateList.get(4);
                coordinateList.clear();
                LineString lineString1 = geomFac.createLineString(lsc);
                MovementTrack movementTrack = new MovementTrack();
                movementTrack.setWkt(wktWriter.write(lineString1));
                movementTrack.setDistance((double) MockingUtils.randInt(0, 50));
                movementTrack.setDuration((double) MockingUtils.randInt(0, 50));
                movementTrack.setId(String.valueOf(i));
                movementTrackList.add(movementTrack);
            }
        }

    }

    private void readPoints() throws IOException {

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {
            br = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("mock/points_1.csv")));
            while ((line = br.readLine()) != null) {
                String[] country = line.split(cvsSplitBy);
                Coordinate coord = new Coordinate(Double.parseDouble(country[1]), Double.parseDouble(country[3]));
                points.add(geometryFactory.createPoint(coord));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

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

package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.io.ParseException;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingActivitySummary;
import eu.europa.ec.fisheries.uvms.reporting.service.util.GeometryUtil;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.util.Date;
import java.util.List;

/**
 * Created by padhyad on 12/6/2016.
 */
public class ActivityDTO {

    public static final SimpleFeatureType ACTIVITY = build();
    private static final String GEOMETRY = "geometry";
    private static final String ACTIVITY_TYPE = "activityType";
    private static final String ACCEPTED_DATE_TIME = "acceptedDateTime";
    private static final String DATA_SOURCE = "dataSource";
    private static final String REPORT_TYPE = "reportType";
    private static final String PURPOSE_CODE = "purposeCode";
    private static final String VESSEL_NAME = "vesselName";
    private static final String GEARS = "gears";
    private static final String SPECIES = "species";
    private static final String PORTS = "ports";
    private static final String AREAS = "areas";
    private static final String VESSEL_IDENTIFIERS = "vesselIdentifiers";
    private static final String ACTIVITIES = "activities";

    private FishingActivitySummary summary;

    public ActivityDTO(FishingActivitySummary summary) {
        this.summary = summary;
    }

    private static SimpleFeatureType build() {
        SimpleFeatureTypeBuilder sb = new SimpleFeatureTypeBuilder();
        sb.setCRS(DefaultGeographicCRS.WGS84);
        sb.setName(ACTIVITIES);
        sb.add(GEOMETRY, MultiPoint.class);
        sb.add(ACTIVITY_TYPE, String.class);
        sb.add(ACCEPTED_DATE_TIME, String.class);
        sb.add(DATA_SOURCE, String.class);
        sb.add(REPORT_TYPE, String.class);
        sb.add(PURPOSE_CODE, String.class);
        sb.add(VESSEL_NAME, String.class);
        sb.add(GEARS, List.class);
        sb.add(SPECIES, List.class);
        sb.add(PORTS, List.class);
        sb.add(AREAS, List.class);
        sb.add(VESSEL_IDENTIFIERS, List.class);
        return sb.buildFeatureType();
    }

    public SimpleFeature toFeature() throws ParseException {
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(ACTIVITY);
        featureBuilder.set(GEOMETRY, GeometryUtil.toGeometry(summary.getGeometry()));
        featureBuilder.set(ACTIVITY_TYPE, summary.getActivityType());
        featureBuilder.set(ACCEPTED_DATE_TIME, summary.getAcceptedDateTime());
        featureBuilder.set(DATA_SOURCE, summary.getDataSource());
        featureBuilder.set(REPORT_TYPE, summary.getReportType());
        featureBuilder.set(PURPOSE_CODE, summary.getPurposeCode());
        featureBuilder.set(VESSEL_NAME, summary.getVesselName());
        featureBuilder.set(GEARS, summary.getGears());
        featureBuilder.set(SPECIES, summary.getSpecies());
        featureBuilder.set(PORTS, summary.getPorts());
        featureBuilder.set(AREAS, summary.getAreas());
        featureBuilder.set(VESSEL_IDENTIFIERS, summary.getVesselIdentifiers());
        return featureBuilder.buildFeature(null);
    }
}

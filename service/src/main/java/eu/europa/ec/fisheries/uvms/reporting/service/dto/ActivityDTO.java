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
import eu.europa.ec.fisheries.uvms.activity.model.schemas.VesselIdentifierSchemeIdEnum;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.VesselIdentifierType;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.reporting.service.util.GeometryUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.util.List;

public class ActivityDTO {

    private static final String GEOMETRY = "geometry";
    private static final String ACTIVITY_TYPE = "activityType";
    private static final String ACTIVITY_ID = "activityId";
    private static final String ACCEPTED_DATE_TIME = "acceptedDateTime";
    private static final String DATA_SOURCE = "dataSource";
    private static final String REPORT_TYPE = "reportType";
    private static final String PURPOSE_CODE = "purposeCode";
    private static final String VESSEL_NAME = "vesselName";
    private static final String VESSEL_GUID = "vesselGuid";
    private static final String TRIPID = "tripId";
    private static final String FLAG_STATE = "flagState";
    private static final String IS_CORRECTION = "isCorrection";
    private static final String GEARS = "gears";
    private static final String SPECIES = "species";
    private static final String PORTS = "ports";
    private static final String AREAS = "areas";
    private static final String ACTIVITIES = "activities";
    public static final SimpleFeatureType ACTIVITY = build();
    private FishingActivitySummaryDTO summary;

    public ActivityDTO(FishingActivitySummaryDTO summary) {
        this.summary = summary;
    }

    private static SimpleFeatureType build() {
        SimpleFeatureTypeBuilder sb = new SimpleFeatureTypeBuilder();
        sb.setCRS(DefaultGeographicCRS.WGS84);
        sb.setName(ACTIVITIES);
        sb.add(GEOMETRY, MultiPoint.class);
        sb.add(ACTIVITY_TYPE, String.class);
        sb.add(ACTIVITY_ID, Integer.class);
        sb.add(VESSEL_GUID, String.class);
        sb.add(TRIPID, String.class);
        sb.add(FLAG_STATE, String.class);
        sb.add(IS_CORRECTION, Boolean.class);
        sb.add(ACCEPTED_DATE_TIME, String.class);
        sb.add(DATA_SOURCE, String.class);
        sb.add(REPORT_TYPE, String.class);
        sb.add(PURPOSE_CODE, String.class);
        sb.add(VESSEL_NAME, String.class);
        sb.add(GEARS, List.class);
        sb.add(SPECIES, List.class);
        sb.add(PORTS, List.class);
        sb.add(AREAS, List.class);

        sb.add(VesselIdentifierSchemeIdEnum.ICCAT.value(), String.class);
        sb.add(VesselIdentifierSchemeIdEnum.GFCM.value(), String.class);
        sb.add(VesselIdentifierSchemeIdEnum.EXT_MARK.value(), String.class);
        sb.add(VesselIdentifierSchemeIdEnum.IRCS.value(), String.class);
        sb.add(VesselIdentifierSchemeIdEnum.CFR.value(), String.class);
        sb.add(VesselIdentifierSchemeIdEnum.UVI.value(), String.class);

        return sb.buildFeatureType();
    }

    public SimpleFeature toFeature() throws ParseException {
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(ACTIVITY);
        featureBuilder.set(GEOMETRY, GeometryUtil.toGeometry(summary.getGeometry()));
        featureBuilder.set(ACTIVITY_TYPE, summary.getActivityType());
        featureBuilder.set(ACTIVITY_ID, summary.getActivityId());
        featureBuilder.set(VESSEL_GUID, summary.getVesselGuid());
        featureBuilder.set(TRIPID, summary.getTripId());
        featureBuilder.set(FLAG_STATE, summary.getFlagState());
        featureBuilder.set(IS_CORRECTION, summary.isCorrection());
        featureBuilder.set(ACCEPTED_DATE_TIME, summary.getAcceptedDateTime() != null ? DateFormatUtils.format(summary.getAcceptedDateTime().toGregorianCalendar(), DateUtils.DATE_TIME_UI_FORMAT) : null);
        featureBuilder.set(DATA_SOURCE, summary.getDataSource());
        featureBuilder.set(REPORT_TYPE, summary.getReportType());
        featureBuilder.set(PURPOSE_CODE, summary.getPurposeCode());
        featureBuilder.set(VESSEL_NAME, summary.getVesselName());
        featureBuilder.set(GEARS, summary.getGears());
        featureBuilder.set(SPECIES, summary.getSpecies());
        featureBuilder.set(PORTS, summary.getPorts());
        featureBuilder.set(AREAS, summary.getAreas());

        List<VesselIdentifierType> vesselIdentifiers = summary.getVesselIdentifiers();
        if (!CollectionUtils.isEmpty(vesselIdentifiers)) {
            for (VesselIdentifierType vesselIdentifierType : vesselIdentifiers) {
                if (!StringUtils.isEmpty(vesselIdentifierType.getValue())) {
                    featureBuilder.set(vesselIdentifierType.getKey().toString(), vesselIdentifierType.getValue());
                }
            }
        }
        return featureBuilder.buildFeature(null);
    }
}

package eu.europa.ec.fisheries.uvms.reporting.service.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MovementDto;
import org.apache.commons.lang3.StringUtils;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.IOException;

public class MovementDtoSerializer extends JsonSerializer<MovementDto> {

    private static final String PROPERTIES = "properties";

    @Override
    public void serialize(MovementDto value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.setName("poi");
        typeBuilder.setDefaultGeometry("location");
        typeBuilder.add("location", Point.class);
        typeBuilder.add("name", String.class);
        typeBuilder.nillable(true).add("etc", String.class);

        SimpleFeatureType featureType = typeBuilder.buildFeatureType();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);

        featureBuilder.add(new GeometryFactory().createPoint(new Coordinate(102.0, 2.0)));
        featureBuilder.add("Hello, World");
        featureBuilder.add(null);
        SimpleFeature feature = featureBuilder.buildFeature("fid-1");
        toJson(feature);

    }

    private void toJson(SimpleFeature feature) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        //mapper.registerModule(new GeoJsonModule());
        mapper.writer().writeValueAsString(feature);
    }

    private void writeProperties(JsonGenerator gen, MovementDto value) throws IOException{
        gen.writeFieldName(PROPERTIES);
        gen.writeStartObject();
        gen.writeStringField("id", value.getId());
        gen.writeStringField("connectID", value.getConnectId());
        gen.writeStringField("status", value.getStatus());
        gen.writeNumberField("calculatedSpeed", value.getCalculatedSpeed());
        gen.writeNumberField("course", value.getCourse());
        gen.writeNumberField("measuredSpeed", value.getMeasuredSpeed());
        gen.writeStringField("messageType", value.getMessageType().value());
        if (value.getPositionTime() != null){
            String s = value.getPositionTime().toString();
            if (StringUtils.isNotBlank(s)){
                gen.writeStringField("positionTime", s);
            }
        }
        gen.writeObjectField("asset", value.getAsset());
        gen.writeEndObject();
    }

    private SimpleFeature buildFeature() {
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.setName("poi");
        typeBuilder.setDefaultGeometry("location");
        typeBuilder.add("location", Point.class);
        typeBuilder.add("name", String.class);
        typeBuilder.nillable(true).add("etc", String.class);

        SimpleFeatureType featureType = typeBuilder.buildFeatureType();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);

        featureBuilder.add(new GeometryFactory().createPoint(new Coordinate(102.0, 2.0)));
        featureBuilder.add("Hello, World");
        featureBuilder.add(null);
        SimpleFeature feature = featureBuilder.buildFeature("fid-1");
        return feature;
        }
}

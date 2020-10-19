package eu.europa.ec.fisheries.uvms.reporting.rest;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.micromata.opengis.kml.v_2_2_0.Data;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.ExtendedData;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.kml.interfaces.ActivityFeatureDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.kml.interfaces.FeatureListsDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.kml.interfaces.FeaturesDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.kml.interfaces.PositionFeatureDto;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.kml.interfaces.SegmentFeatureDTO;
import org.apache.commons.io.IOUtils;

@WebServlet(
		asyncSupported = true,
		urlPatterns = "/rest/kml/*"
)
public class KmlServlet extends HttpServlet {

	private static final String FILE_OPENING = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
			"<ns2:kml xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:ns2=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\" xmlns:xal=\"urn:oasis:names:tc:ciq:xsdschema:xAL:2.0\">\n" +
			"    <ns2:Document>\n";	
	
	private static final String FILE_ENDING = "    </ns2:Document>\n</ns2:kml>\n";

	private static final String OPENING_TAG = "<ns2:Document>";
	
	private Map<String, Consumer<String>> consumers = new ConcurrentHashMap<>();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		final PrintWriter out = resp.getWriter();
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setContentType("text/plain");
		out.print(UUID.randomUUID());
		out.flush();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		if( pathInfo == null || "/".equals(pathInfo) ) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		else {
			String uuid = pathInfo.substring(1);
			resp.setContentType("text/xml");
			resp.setHeader("Content-Disposition", "attachment; filename=\"" + uuid + ".kml\"");
			resp.getWriter().print(FILE_OPENING);
			AsyncContext asyncCtx = req.startAsync();
			asyncCtx.addListener(new AsyncListener() {
				@Override
				public void onComplete(AsyncEvent event) throws IOException {

				}

				@Override
				public void onTimeout(AsyncEvent event) throws IOException {
					consumers.remove(uuid);
				}

				@Override
				public void onError(AsyncEvent event) throws IOException {
					if( event.getThrowable() instanceof UncheckedIOException) {
						throw ((UncheckedIOException) event.getThrowable()).getCause();
					}
					consumers.remove(uuid);
				}

				@Override
				public void onStartAsync(AsyncEvent event) throws IOException {

				}
			});
			asyncCtx.setTimeout(305000);
			consumers.put(uuid, makeConsumer(uuid, asyncCtx));
			resp.getWriter().flush();
		}
	}

	private Consumer<String> makeConsumer(String uuid, AsyncContext asyncCtx) {
		return message -> {
			try {
				if( message != null && message.length() > 0 ) {
					PrintWriter out = asyncCtx.getResponse().getWriter();
					message = exportFolderToKML(message);
					out.print(message.substring(message.indexOf(OPENING_TAG) + OPENING_TAG.length(), message.lastIndexOf("</ns2:Document>")));
					out.flush();
				} else {
					asyncCtx.getResponse().getWriter().println(FILE_ENDING);
					asyncCtx.getResponse().getWriter().flush();
					asyncCtx.complete();
					consumers.remove(uuid);
				}
			} catch (IOException | JAXBException ioe) {
				throw new RuntimeException(ioe);
			}
		};
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		if( pathInfo == null || "/".equals(pathInfo) ) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		else {
			String uuid = pathInfo.substring(1);
			Consumer<String> consumer = consumers.get(uuid);
			if( consumer == null ) {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			} else {
				consumer.accept(IOUtils.toString(req.getInputStream(), StandardCharsets.UTF_8));
			}
		}
	}



	private void createActivityPlacemark(Folder folder, ActivityFeatureDTO dto) {
		Placemark placemark = folder.createAndAddPlacemark();
		placemark.withName("Fishing Activity: " + dto.getActivityId());
		ExtendedData extendedData = new ExtendedData();
		extendedData.addToData(createData("activityId", String.valueOf(dto.getActivityId())));
		extendedData.addToData(createData("faReportID", String.valueOf(dto.getFaReportID())));
		extendedData.addToData(createData("activityType", dto.getActivityType()));
		extendedData.addToData(createData("acceptedDateTime", dto.getAcceptedDateTime()));
		extendedData.addToData(createData("dataSource", dto.getDataSource()));
		extendedData.addToData(createData("reportType", dto.getReportType()));
		extendedData.addToData(createData("purposeCode", dto.getPurposeCode()));
		extendedData.addToData(createData("vesselName", dto.getVesselName()));
		extendedData.addToData(createData("vesselGuid", dto.getVesselGuid()));
		extendedData.addToData(createData("tripId", dto.getTripId()));
		extendedData.addToData(createData("flagState", dto.getFlagState()));
		extendedData.addToData(createData("isCorrection", String.valueOf(dto.isCorrection())));
		dto.getVesselIdentifiers().stream()
				.map(identifier -> createData(identifier.getKey().name(), identifier.getValue()))
				.forEach(extendedData::addToData);
		extendedData.addToData(createData("gears", String.join(",", dto.getGears())));
		extendedData.addToData(createData("species", String.join(",", dto.getSpecies())));
		extendedData.addToData(createData("ports", String.join(",", dto.getPorts())));
		extendedData.addToData(createData("areas", String.join(",", dto.getAreas())));
		placemark.setExtendedData(extendedData);
		placemark.createAndSetPoint().addToCoordinates(dto.getGeometry());
	}

	private void createPositionPlacemark(Folder folder, PositionFeatureDto dto) {
		Placemark placemark = folder.createAndAddPlacemark();
		placemark.withName("Position: " + dto.getMovementGuid());
		ExtendedData extendedData = new ExtendedData();
		extendedData.addToData(createData("positionTime", dto.getPositionTime()));
		extendedData.addToData(createData("connectionId", dto.getConnectionId()));
		extendedData.addToData(createData("reportedCourse", dto.getReportedCourse()));
		extendedData.addToData(createData("movementType", dto.getMovementType()));
		extendedData.addToData(createData("reportedSpeed", dto.getReportedSpeed()));
		extendedData.addToData(createData("cfr", dto.getCfr()));
		extendedData.addToData(createData("countryCode", dto.getCountryCode()));
		extendedData.addToData(createData("calculatedSpeed", dto.getCalculatedSpeed()));
		extendedData.addToData(createData("ircs", dto.getIrcs()));
		extendedData.addToData(createData("name", dto.getName()));
		extendedData.addToData(createData("movementGuid", dto.getMovementGuid()));
		extendedData.addToData(createData("externalMarking", dto.getExternalMarking()));
		extendedData.addToData(createData("source", dto.getSource()));
		extendedData.addToData(createData("isVisible", dto.getIsVisible()));
		placemark.setExtendedData(extendedData);
		placemark.getStyleSelector();
		placemark.createAndAddStyle().createAndSetIconStyle().withColor(buildColorValue(dto.getColor())).withHeading(0.0).withScale(1.0);
		placemark.createAndSetPoint().addToCoordinates(dto.getGeometry());
	}

	private void createSegmentPlacemark(Folder folder, SegmentFeatureDTO dto) {
		Placemark placemark = folder.createAndAddPlacemark();
		placemark.withName("Segment: " + dto.getTrackId());
		ExtendedData extendedData = new ExtendedData();
		extendedData.addToData(createData("cfr", dto.getCfr()));
		extendedData.addToData(createData("countryCode", dto.getCountryCode()));
		extendedData.addToData(createData("courseOverGround", dto.getCourseOverGround()));
		extendedData.addToData(createData("distance", dto.getDistance()));
		extendedData.addToData(createData("duration", dto.getDistance()));
		extendedData.addToData(createData("externalMarking", dto.getExternalMarking()));
		extendedData.addToData(createData("ircs", dto.getIrcs()));
		extendedData.addToData(createData("name", dto.getName()));
		extendedData.addToData(createData("segmentCategory", dto.getSegmentCategory()));
		extendedData.addToData(createData("speedOverGround", dto.getSpeedOverGround()));
		extendedData.addToData(createData("trackId", dto.getTrackId()));
		placemark.setExtendedData(extendedData);
		placemark.createAndAddStyle().createAndSetLineStyle().withWidth(5.0).withColor(buildColorValue(dto.getColor()));
		placemark.createAndSetTrack().setCoord(dto.getGeometry());
	}

	private String buildColorValue(String colorInHex6) {
		Color rgbColor = Color.decode(colorInHex6);
		return "ff" + Integer.toHexString(rgbColor.getBlue()) + Integer.toHexString(rgbColor.getGreen()) + Integer.toHexString(rgbColor.getRed());
	}

	private Data createData(String name, String value) {
		Data data = new Data(value);
		data.setName(name);
		return data;
	}
	
	private String exportFolderToKML(String message) throws IOException, JAXBException {
		ObjectMapper mapper = new ObjectMapper();
		FeaturesDTO features = mapper.readValue(message, FeaturesDTO.class);

		final Kml kml = new Kml();
		Document doc = kml.createAndSetDocument();
		for(Map.Entry<String, Map<String, FeatureListsDTO>> flagStateEntry: features.getFeatures().entrySet()) {
			Folder flagStateFolder = doc.createAndAddFolder();
			flagStateFolder.withName(flagStateEntry.getKey());
			for(Map.Entry<String, FeatureListsDTO> identifierEntry: flagStateEntry.getValue().entrySet()) {
				Folder identifierFolder = flagStateFolder.createAndAddFolder();
				identifierFolder.withName(identifierEntry.getKey());
				FeatureListsDTO featureListsDTO = identifierEntry.getValue();
				if(featureListsDTO.getActivities() != null){
					featureListsDTO.getActivities().forEach(dto -> createActivityPlacemark(identifierFolder, dto));
				}
				if(featureListsDTO.getPositions() != null){
					featureListsDTO.getPositions().forEach(dto -> createPositionPlacemark(identifierFolder, dto));
				}
				if(featureListsDTO.getSegments() != null){
					featureListsDTO.getSegments().forEach(dto -> createSegmentPlacemark(identifierFolder, dto));
				}
			}
		}
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		kml.marshal(byteArrayOutputStream);
		return byteArrayOutputStream.toString();
	}
}

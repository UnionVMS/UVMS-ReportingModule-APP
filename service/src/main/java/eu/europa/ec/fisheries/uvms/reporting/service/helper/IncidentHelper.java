package eu.europa.ec.fisheries.uvms.reporting.service.helper;

import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;
import eu.europa.ec.fisheries.uvms.asset.client.AssetClient;
import eu.europa.ec.fisheries.uvms.asset.client.model.AssetDTO;
import eu.europa.ec.fisheries.uvms.asset.client.model.AssetIdentifier;
import eu.europa.ec.fisheries.uvms.movement.client.model.MicroMovement;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.entities.Incident;
import eu.europa.ec.fisheries.uvms.reporting.service.domain.enums.StatusEnum;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.time.Instant;
import java.util.UUID;

@Stateless
public class IncidentHelper {

    @EJB
    private AssetClient assetClient;

    public Incident constructIncident(TicketType ticket, MicroMovement movement) {
        Incident incident = new Incident();
        incident.setMobileTerminalId(UUID.fromString(ticket.getMobileTerminalGuid()));
        incident.setCreateDate(Instant.now());
        incident.setStatus(StatusEnum.valueOf(ticket.getStatus().name()));
        incident.setTicketId(UUID.fromString(ticket.getGuid()));
        incident.setLatitude(movement.getLocation().getLatitude());
        incident.setLongitude(movement.getLocation().getLongitude());
        incident.setAltitude(movement.getLocation().getAltitude());
        setAssetValues(incident, ticket.getAssetGuid());
        return incident;
    }

    private void setAssetValues(Incident incident, String assetId) {
        AssetDTO asset = assetClient.getAssetById(AssetIdentifier.GUID, assetId);
        incident.setAssetId(asset.getId());
        incident.setAssetName(asset.getName());
        incident.setIrcs(asset.getIrcs());
    }
}

package eu.europa.ec.fisheries.uvms.reporting.service.domain.dto;

import java.io.Serializable;

public class StatusDto implements Serializable {

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

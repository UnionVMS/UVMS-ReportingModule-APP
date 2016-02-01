package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthenticationResponse implements Serializable {

    private static final long serialVersionUID = 3286176483015988847L;

    private String jwToken; //NOSONAR
    private boolean authenticated; //NOSONAR
    private int statusCode; //NOSONAR
}

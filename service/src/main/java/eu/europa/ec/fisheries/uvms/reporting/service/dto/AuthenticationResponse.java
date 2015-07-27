package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import java.io.Serializable;

/**
 * Created by georgige on 7/24/2015.
 */
public class AuthenticationResponse implements Serializable {
    private static final long serialVersionUID = 3286176483015988847L;

    private String JWToken;
    private boolean authenticated;
    private int statusCode;


    public String getJwtoken() {
        return JWToken;
    }

    public void setJwtoken(String JWToken) {
        this.JWToken = JWToken;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "AuthenticationResponse{" +
                "JWToken='" + JWToken + '\'' +
                ", authenticated=" + authenticated +
                ", statusCode=" + statusCode +
                '}';
    }
}

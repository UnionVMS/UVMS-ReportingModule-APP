package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import java.io.Serializable;

/**
 * Created by georgige on 7/24/2015.
 */
public class AuthenticationRequest implements Serializable{

    private static final long serialVersionUID = -8088379939241999781L;

    private String userName, password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

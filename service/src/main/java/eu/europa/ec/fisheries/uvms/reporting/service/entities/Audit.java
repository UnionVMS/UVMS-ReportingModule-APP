package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class Audit implements Serializable {

    public static final String CREATED_ON = "created_on";

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = CREATED_ON, nullable = false)
    private Date createdOn;

    Audit() {
    }

    @Builder
    public Audit(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
}

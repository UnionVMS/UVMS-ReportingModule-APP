package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import lombok.*;

import javax.persistence.*;
import java.io.*;
import java.util.*;

@Embeddable
@EqualsAndHashCode
@ToString
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

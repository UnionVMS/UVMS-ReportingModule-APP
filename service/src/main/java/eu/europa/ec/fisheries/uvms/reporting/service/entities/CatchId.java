package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CatchId implements Serializable {
    private Long activity;
    private String speciesCode;
}

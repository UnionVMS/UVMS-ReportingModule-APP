package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import java.util.List;

public class VmsDto {

    private List<MovementDto> movements;
    private List<SegmentDto> segments;

    public VmsDto(List<MovementDto> movements, List<SegmentDto> segments) {
        this.segments = segments;
        this.movements = movements;
    }

    public List<MovementDto> getMovements() {
        return movements;
    }

    public void setMovements(List<MovementDto> movements) {
        this.movements = movements;
    }

    public List<SegmentDto> getSegments() {
        return segments;
    }

    public void setSegments(List<SegmentDto> segments) {
        this.segments = segments;
    }
}

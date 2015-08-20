package eu.europa.ec.fisheries.uvms.reporting.service.dto;

import java.util.List;

/**
 * //TODO create test
 */
public class TrackDto {

    private List<SegmentDto> segments;

    public List<SegmentDto> getSegments() {
        return segments;
    }

    public void setSegments(List<SegmentDto> segments) {
        this.segments = segments;
    }
}

package eu.europa.ec.fisheries.uvms.reporting.service.entities;

public enum FilterType {

    VESSEL, VGROUP, DATETIME, VMSPOS;

    public static FilterType type(Filter filter){
        FilterType type = null;

        if(filter instanceof VesselFilter){
            type = VESSEL;
        }
        else if (filter instanceof VesselGroupFilter){
            type = VGROUP;
        }
        else if (filter instanceof DateTimeFilter){
            type = DATETIME;
        }
        else if (filter instanceof VmsPositionFilter){
            type = VMSPOS;
        }
        return type;
    }
}

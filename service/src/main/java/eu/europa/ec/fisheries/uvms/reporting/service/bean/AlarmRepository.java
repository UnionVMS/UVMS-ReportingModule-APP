package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.Alarm;

public interface AlarmRepository {

    Alarm createAlarmEntity(Alarm entity);

    Alarm findAlarmByGuid(String guid);

    Alarm updateAlarmEntity(Alarm entity);
}

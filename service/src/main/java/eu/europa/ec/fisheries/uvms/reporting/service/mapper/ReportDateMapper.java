package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.reporting.model.schemas.ReportGetStartAndEndDateRS;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.CommonFilter;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Position;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Selector;
import lombok.Builder;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by padhyad on 3/17/2016.
 */
public class ReportDateMapper {

    private Date now;

    @Builder(builderMethodName = "ReportDateMapperBuilder")
    public ReportDateMapper(Date now) {
        this.now = now;
    }

    public ReportGetStartAndEndDateRS getReportDates(CommonFilter filter) {
        ReportGetStartAndEndDateRS response = new ReportGetStartAndEndDateRS();
        Date startDate = null;
        Date endDate = null;
        if (filter.getPositionSelector().getSelector().equals(Selector.all)) {
            startDate = filter.getDateRange().getStartDate();
            endDate = filter.getDateRange().getEndDate();
        } else if (filter.getPositionSelector().getSelector().equals(Selector.last)) {
            if (filter.getPositionSelector().getPosition().equals(Position.hours)) {
                DateTime dateTime = new DateTime(now);
                dateTime = dateTime.minusHours(filter.getPositionSelector().getValue().intValue());
                startDate = dateTime.toDate();
                endDate = now;
            } else if (filter.getPositionSelector().getPosition().equals(Position.positions)) {
                startDate = filter.getDateRange().getStartDate();
                endDate = now;
            }
        }
        response.setStartDate(startDate == null ? null : new SimpleDateFormat(DateUtils.DATE_TIME_UI_FORMAT).format(startDate));
        response.setEndDate(new SimpleDateFormat(DateUtils.DATE_TIME_UI_FORMAT).format(endDate));

        return response;
    }
}

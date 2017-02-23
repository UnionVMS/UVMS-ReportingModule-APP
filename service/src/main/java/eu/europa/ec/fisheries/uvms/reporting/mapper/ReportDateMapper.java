/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.mapper;

import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.reporting.model.schemas.ReportGetStartAndEndDateRS;
import eu.europa.ec.fisheries.uvms.reporting.entities.CommonFilter;
import eu.europa.ec.fisheries.uvms.reporting.entities.Position;
import eu.europa.ec.fisheries.uvms.reporting.entities.Selector;
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
/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.service.entities.converter;

import eu.europa.ec.fisheries.uvms.reporting.service.mapper.GroupCriteriaFilterMapper;
import eu.europa.ec.fisheries.uvms.reporting.service.type.GroupCriteriaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.apache.commons.lang3.StringUtils;

@Converter(autoApply=true)
public class GroupCriteriaFilterConverter implements AttributeConverter<List, String> {

    @Override
    public String convertToDatabaseColumn(List groupCriteriaTypes) {
        List<String> strings = GroupCriteriaFilterMapper.INSTANCE.mapStringListToGroupCriteriaTypeList(groupCriteriaTypes);
        return StringUtils.join(strings, ",");
    }

    @Override
    public List<GroupCriteriaType> convertToEntityAttribute(String dbData) {
        if (StringUtils.isBlank(dbData)) {
            return new ArrayList<>();
        }
        List <String> strings = Arrays.asList(dbData.split(","));
        return GroupCriteriaFilterMapper.INSTANCE.mapGroupCriteriaTypeListToStringList(strings);
    }
}

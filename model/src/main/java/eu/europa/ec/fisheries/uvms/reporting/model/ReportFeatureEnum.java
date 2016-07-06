/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.model;

public enum ReportFeatureEnum {

    LIST_REPORTS("LIST_REPORTS"),
    SHARE_REPORT_SCOPE("SHARE_REPORT_SCOPE"),
    SHARE_REPORT_PUBLIC("SHARE_REPORT_PUBLIC"),
    CREATE_REPORT("CREATE_REPORT"),
    MANAGE_ALL_REPORTS("MANAGE_ALL_REPORTS");

    String value;

    ReportFeatureEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
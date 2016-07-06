/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.model;

public enum VisibilityEnum {
	
	PRIVATE("private"),
	SCOPE("scope"),
	PUBLIC("public");

    private String name;

    VisibilityEnum(String value) {
        this.name = value;
    }

    public String getName() {
        return name;
    }

    public static VisibilityEnum getByName(String name) {
        for(VisibilityEnum e: VisibilityEnum.values()) {
            if(e.name.equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;// not found
    }
}
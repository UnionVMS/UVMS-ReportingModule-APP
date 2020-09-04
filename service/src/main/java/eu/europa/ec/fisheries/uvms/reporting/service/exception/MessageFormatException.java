/*
 *
 *  Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2020.
 *
 *  This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 *  and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 *  the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 *  details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *  
 */

/*
 *
 *  Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2020.
 *
 *  This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 *  and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 *  the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 *  details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package eu.europa.ec.fisheries.uvms.reporting.service.exception;

/**
 * Indicate an error in message format, e.g. a parsing error.
 */
public class MessageFormatException extends ApplicationException {
    public MessageFormatException() {
    }

    public MessageFormatException(String message) {
        super(message);
    }

    public MessageFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageFormatException(Throwable cause) {
        super(cause);
    }
}

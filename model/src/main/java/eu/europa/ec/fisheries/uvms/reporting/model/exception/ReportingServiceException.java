/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.model.exception;

/**
 * 
 * Reporting Exception extending exception for handling all exception in the Reporting module
 * <strong>SERVICE</strong> layer must return this exception to <strong>REST</strong> layer
 * @see {@link Exception}
 *
 */
public final class ReportingServiceException extends Exception {
	
	private static final long serialVersionUID = -5238088781317596825L;
	
	public ReportingServiceException(Throwable e) {
		super(e);
	}

	public ReportingServiceException(String msg) {
		super(msg);
	}
	
	public ReportingServiceException(String msg, Throwable e) {
		super(msg, e);
	}
}
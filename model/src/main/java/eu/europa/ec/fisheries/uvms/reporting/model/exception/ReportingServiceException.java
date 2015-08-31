package eu.europa.ec.fisheries.uvms.reporting.model.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

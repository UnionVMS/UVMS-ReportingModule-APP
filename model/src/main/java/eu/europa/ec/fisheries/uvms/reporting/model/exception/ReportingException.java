package eu.europa.ec.fisheries.uvms.reporting.model.exception;

public class ReportingException extends Exception {
    private static final long serialVersionUID = 1L;

    public ReportingException(String message) {
        super(message);
    }

    public ReportingException(String message, Throwable cause) {
        super(message, cause);
    }

}

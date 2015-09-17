package eu.europa.ec.fisheries.uvms.reporting.model.exception;

public class ReportingModelException extends ReportingException {
    private static final long serialVersionUID = 1L;

    public ReportingModelException(String message) {
        super(message);
    }

    public ReportingModelException(String message, Throwable cause) {
        super(message, cause);
    }
}

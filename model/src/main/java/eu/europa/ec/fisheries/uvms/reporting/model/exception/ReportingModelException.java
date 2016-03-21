package eu.europa.ec.fisheries.uvms.reporting.model.exception;

/**
 * Created by padhyad on 3/18/2016.
 */
public class ReportingModelException extends Exception {

    private static final long serialVersionUID = 7582161942682172612L;

    public ReportingModelException(String message) {
        super(message);
    }

    public ReportingModelException(Throwable cause) {
        super(cause);
    }

    public ReportingModelException(String message, Throwable cause) {
        super(message, cause);
    }
}

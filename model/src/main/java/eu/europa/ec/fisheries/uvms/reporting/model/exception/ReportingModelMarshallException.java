package eu.europa.ec.fisheries.uvms.reporting.model.exception;

public class ReportingModelMarshallException extends ReportingModelMapperException  {
    private static final long serialVersionUID = 7582161942682172612L;

    public ReportingModelMarshallException(String message) {
        super(message);
    }

    public ReportingModelMarshallException(String message, Throwable cause) {
        super(message, cause);
    }

}

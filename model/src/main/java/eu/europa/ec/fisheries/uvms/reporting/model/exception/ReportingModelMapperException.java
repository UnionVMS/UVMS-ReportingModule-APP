package eu.europa.ec.fisheries.uvms.reporting.model.exception;

public class ReportingModelMapperException extends ReportingModelException {
    private static final long serialVersionUID = 1L;

    public ReportingModelMapperException(String message) {
        super(message);
    }

    public ReportingModelMapperException(String message, Throwable cause) {
        super(message, cause);
    }

}

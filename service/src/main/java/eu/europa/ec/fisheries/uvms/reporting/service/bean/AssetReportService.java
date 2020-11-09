package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.wsdl.asset.types.Asset;

public interface AssetReportService {

    void createOrUpdate(Asset asset);
}

package eu.europa.ec.fisheries.uvms.reporting.service.bean;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.Asset;

public interface AssetRepository {

    Asset createAssetEntity(Asset entity);

    Asset findAssetByAssetHistoryGuid(String guid);

    int updateHistoryRecordsAsInactiveForAssetGuid(String guid);

    int makeOtherHistoryEntriesOfAssetInactiveExceptCurrentHistId(String guid, String eventId);
}

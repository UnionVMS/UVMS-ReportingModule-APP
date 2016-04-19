package eu.europa.ec.fisheries.uvms.reporting.service.entities;

public interface FilterVisitor<T> {

    T visitVmsTrackFilter(VmsTrackFilter trackFilter);
    T visitVmsSegmentFilter(VmsSegmentFilter segmentFilter);
    T visitVmsPositionFilter(VmsPositionFilter positionFilter);
    T visitAssetFilter(AssetFilter assetFilter);
    T visitAssetGroupFilter(AssetGroupFilter assetGroupFilter);
    T visitAreaFilter(AreaFilter areaFilter);
    T visitCommonFilter(CommonFilter commonFilter);

}

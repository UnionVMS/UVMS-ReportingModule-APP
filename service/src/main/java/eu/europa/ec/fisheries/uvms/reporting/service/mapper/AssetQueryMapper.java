package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.asset.client.model.AssetDTO;
import eu.europa.ec.fisheries.uvms.asset.client.model.AssetQuery;
import eu.europa.ec.fisheries.wsdl.asset.group.AssetGroup;
import eu.europa.ec.fisheries.wsdl.asset.group.AssetGroupSearchField;
import eu.europa.ec.fisheries.wsdl.asset.types.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

public class AssetQueryMapper {

    public static AssetQuery assetListQueryToAssetQuery(AssetListQuery assetListQuery) {
        AssetQuery query = new AssetQuery();
        instantiateAllCollections(query);

        List<AssetListCriteriaPair> criteriaPairs = assetListQuery.getAssetSearchCriteria().getCriterias();

        for(AssetListCriteriaPair criteria : criteriaPairs) {
            ConfigSearchField key = criteria.getKey();

            switch (key) {
                case FLAG_STATE:
                    query.getFlagState().add(criteria.getValue());
                    break;
                case EXTERNAL_MARKING:
                    query.getExternalMarking().add(criteria.getValue());
                    break;
                case NAME:
                    query.getName().add(criteria.getValue());
                    break;
                case IRCS:
                    query.getIrcs().add(criteria.getValue());
                    break;
                case CFR:
                    query.getCfr().add(criteria.getValue());
                    break;
                case MMSI:
                    query.getMmsi().add(criteria.getValue());
                    break;
                case GUID:
                    UUID uuid = UUID.fromString(criteria.getValue());
                    query.getId().add(uuid);
                    break;
                case HIST_GUID:
                    UUID historyId = UUID.fromString(criteria.getValue());
                    query.getHistoryId().add(historyId);
                    break;
                case DATE:
                    query.setDate(Instant.parse(criteria.getValue()));
                    break;
                case ICCAT:
                    query.getIccat().add(criteria.getValue());
                    break;
                case UVI:
                    query.getUvi().add(criteria.getValue());
                    break;
                case GFCM:
                    query.getGfcm().add(criteria.getValue());
                    break;
                case HOMEPORT:
                    query.getPortOfRegistration().add(criteria.getValue());
                    break;
                case ASSET_TYPE: // No counterpart in AssetQuery
                    break;
                case LICENSE_TYPE:
                    query.getLicenseType().add(criteria.getValue());
                    break;
                case PRODUCER_NAME:
                    query.getProducerName().add(criteria.getValue());
                    break;
                case IMO:
                    query.getImo().add(criteria.getValue());
                    break;
                case GEAR_TYPE:
                    query.setGearType(criteria.getValue());
                    break;
                case MIN_LENGTH:
                    query.setMinLength(Double.valueOf(criteria.getValue()));
                    break;
                case MAX_LENGTH:
                    query.setMaxLength(Double.valueOf(criteria.getValue()));
                    break;
                case MIN_POWER:
                    query.setMinPower(Double.valueOf(criteria.getValue()));
                    break;
                case MAX_POWER:
                    query.setMaxPower(Double.valueOf(criteria.getValue()));
                    break;
                default:
                    throw new RuntimeException("Unknown ConfigSearchField. " +
                            "Key = [" + key + "] Value: [" + criteria.getValue() + "]");
            }
        }
        return query;
    }

    public static List<Asset> dtoListToAssetList(List<AssetDTO> assetDTOList) {

        List<Asset> assetList = new ArrayList<>();

        for(AssetDTO dto : assetDTOList) {
            Asset asset = new Asset();

            AssetId assetId = new AssetId();
            assetId.setType(AssetIdType.GUID);
            assetId.setGuid(dto.getId().toString());
            assetId.setValue(dto.getId().toString());
            asset.setAssetId(assetId);

            if(dto.getActive() != null) asset.setActive(dto.getActive());
            if(dto.getSource() != null) asset.setSource(CarrierSource.fromValue(dto.getSource()));

            AssetHistoryId historyId = new AssetHistoryId();
            historyId.setEventId(dto.getHistoryId().toString());
            asset.setEventHistory(historyId);

            if(dto.getName() != null) asset.setName(dto.getName());
            if(dto.getFlagStateCode() != null) asset.setCountryCode(dto.getFlagStateCode());
            if(dto.getGearFishingType() != null) asset.setGearType(dto.getGearFishingType());
            if(dto.getIrcs() != null) asset.setIrcs(dto.getIrcs());
            if(dto.getExternalMarking() != null) asset.setExternalMarking(dto.getExternalMarking());
            if(dto.getCfr() != null) asset.setCfr(dto.getCfr());
            if(dto.getImo() != null) asset.setImo(dto.getImo());
            if(dto.getMmsi() != null) asset.setMmsiNo(dto.getMmsi());
            if(dto.getHasLicence() != null) asset.setHasLicense(dto.getHasLicence());
            if(dto.getLicenceType() != null) asset.setLicenseType(dto.getLicenceType());
            if(dto.getPortOfRegistration() != null) asset.setHomePort(dto.getPortOfRegistration());
            if(dto.getLengthOverAll() != null) asset.setLengthOverAll(BigDecimal.valueOf(dto.getLengthOverAll()));
            if(dto.getLengthBetweenPerpendiculars() != null)
                asset.setLengthBetweenPerpendiculars(BigDecimal.valueOf(dto.getLengthBetweenPerpendiculars()));
            if(dto.getGrossTonnage() != null) asset.setGrossTonnage(BigDecimal.valueOf(dto.getGrossTonnage()));
            if(dto.getGrossTonnageUnit() != null) asset.setGrossTonnageUnit(dto.getGrossTonnageUnit());
            if(dto.getOtherTonnage() != null) asset.setOtherGrossTonnage(BigDecimal.valueOf(dto.getOtherTonnage()));
            if(dto.getSafteyGrossTonnage() != null) asset.setSafetyGrossTonnage(BigDecimal.valueOf(dto.getSafteyGrossTonnage()));
            if(dto.getPowerOfMainEngine() != null) asset.setPowerMain(BigDecimal.valueOf(dto.getPowerOfMainEngine()));
            if(dto.getPowerOfAuxEngine() != null) asset.setPowerAux(BigDecimal.valueOf(dto.getPowerOfAuxEngine()));

            AssetProdOrgModel prodOrgModel = new AssetProdOrgModel();
            if(dto.getProdOrgName() != null) prodOrgModel.setName(dto.getProdOrgName());
            if(dto.getProdOrgCode() != null) prodOrgModel.setCode(dto.getProdOrgCode());
            asset.setProducer(prodOrgModel);

            if(dto.getIccat() != null) asset.setIccat(dto.getIccat());
            if(dto.getUvi() != null) asset.setUvi(dto.getUvi());
            if(dto.getGfcm() != null) asset.setGfcm(dto.getGfcm());

            assetList.add(asset);
        }
        return assetList;
    }

    public static List<UUID> getGroupListIds(Set<AssetGroup> assetGroupList) {
        List<UUID> uuidList = new ArrayList<>();
        for(AssetGroup ag : assetGroupList) {
            List<AssetGroupSearchField> searchFields = ag.getSearchFields();
            for(AssetGroupSearchField field : searchFields) {
                if(field.getKey().equals(ConfigSearchField.GUID)) {
                    uuidList.add(UUID.fromString(field.getValue()));
                }
            }
        }
        return uuidList;
    }

    public static Map<String, Asset> assetListToAssetMap(List<Asset> assetList) {
        Map<String, Asset> map = new HashMap<>();
        for (Asset asset : assetList) {
            map.put(asset.getEventHistory().getEventId(), asset);
        }
        return map;
    }

    private static void instantiateAllCollections(AssetQuery query) {
        query.setId(new ArrayList<>());
        query.setHistoryId(new ArrayList<>());
        query.setCfr(new ArrayList<>());
        query.setIrcs(new ArrayList<>());
        query.setMmsi(new ArrayList<>());
        query.setImo(new ArrayList<>());
        query.setIccat(new ArrayList<>());
        query.setUvi(new ArrayList<>());
        query.setGfcm(new ArrayList<>());
        query.setName(new ArrayList<>());
        query.setFlagState(new ArrayList<>());
        query.setExternalMarking(new ArrayList<>());
        query.setPortOfRegistration(new ArrayList<>());
        query.setLicenseType(new ArrayList<>());
        query.setProducerName(new ArrayList<>());
    }
}

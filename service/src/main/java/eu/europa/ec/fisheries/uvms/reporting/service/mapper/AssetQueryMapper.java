package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.asset.client.model.AssetDTO;
import eu.europa.ec.fisheries.uvms.asset.client.model.search.SearchBranch;
import eu.europa.ec.fisheries.uvms.asset.client.model.search.SearchFields;
import eu.europa.ec.fisheries.wsdl.asset.types.*;

import java.math.BigDecimal;
import java.util.*;

public class AssetQueryMapper {

    public static SearchBranch assetListQueryToAssetQuery(AssetListQuery assetListQuery) {
        SearchBranch query = new SearchBranch(false);

        List<AssetListCriteriaPair> criteriaPairs = assetListQuery.getAssetSearchCriteria().getCriterias();

        for(AssetListCriteriaPair criteria : criteriaPairs) {
            ConfigSearchField key = criteria.getKey();

            switch (key) {
                case FLAG_STATE:
                    query.addNewSearchLeaf(SearchFields.FLAG_STATE, criteria.getValue());
                    break;
                case EXTERNAL_MARKING:
                    query.addNewSearchLeaf(SearchFields.EXTERNAL_MARKING, criteria.getValue());
                    break;
                case NAME:
                    query.addNewSearchLeaf(SearchFields.NAME, criteria.getValue());
                    break;
                case IRCS:
                    query.addNewSearchLeaf(SearchFields.IRCS, criteria.getValue());
                    break;
                case CFR:
                    query.addNewSearchLeaf(SearchFields.CFR, criteria.getValue());
                    break;
                case MMSI:
                    query.addNewSearchLeaf(SearchFields.MMSI, criteria.getValue());
                    break;
                case GUID:
                    query.addNewSearchLeaf(SearchFields.GUID, criteria.getValue());
                    break;
                case HIST_GUID:
                    query.addNewSearchLeaf(SearchFields.HIST_GUID, criteria.getValue());
                    break;
//                case DATE:
//                    query.setDate(Instant.parse(criteria.getValue()));
//                    break;
                case ICCAT:
                    query.addNewSearchLeaf(SearchFields.ICCAT, criteria.getValue());
                    break;
                case UVI:
                    query.addNewSearchLeaf(SearchFields.UVI, criteria.getValue());
                    break;
                case GFCM:
                    query.addNewSearchLeaf(SearchFields.GFCM, criteria.getValue());
                    break;
                case HOMEPORT:
                    query.addNewSearchLeaf(SearchFields.HOMEPORT, criteria.getValue());
                    break;
                case ASSET_TYPE: // No counterpart in AssetQuery
                    break;
                case LICENSE_TYPE:
                    query.addNewSearchLeaf(SearchFields.LICENSE, criteria.getValue());
                    break;
                case PRODUCER_NAME:
                    query.addNewSearchLeaf(SearchFields.PRODUCER_NAME, criteria.getValue());
                    break;
                case IMO:
                    query.addNewSearchLeaf(SearchFields.IMO, criteria.getValue());
                    break;
                case GEAR_TYPE:
                    query.addNewSearchLeaf(SearchFields.GEAR_TYPE, criteria.getValue());
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

    public static Map<String, Asset> assetListToAssetMap(List<Asset> assetList) {
        Map<String, Asset> map = new HashMap<>();
        for (Asset asset : assetList) {
            map.put(asset.getAssetId().getGuid(), asset);
        }
        return map;
    }
}

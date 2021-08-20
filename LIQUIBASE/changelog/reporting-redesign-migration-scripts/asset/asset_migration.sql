INSERT INTO reporting.asset (asset_hist_guid, asset_guid, asset_hist_active, uvi, iccat, cfr, ircs, name, ext_mark, gfcm, country_code, length_overall, main_gear_type)
SELECT ah.assethist_id,
       aa.asset_guid,
       ah.assethist_active,
       ah.assethist_uvi,
       ah.assethist_iccat,
       ah.assethist_cfr,
       ah.assethist_ircs,
       ah.assethist_nameofasset,
       ah.assethist_externalmarking,
       ah.assethist_gfcm,
       ah.assethist_countryregistration,
       ah.assethist_loa,
       fgt.fishgtyp_desc
FROM asset.assethistory ah
INNER JOIN asset.asset aa ON aa.asset_id = ah.assethist_asset_id
LEFT JOIN asset.fishinggear fg ON fg.fishg_id = ah.assethist_main_fishgear_id
LEFT JOIN asset.fishinggeartype fgt ON fgt.fishgtyp_id = fg.fishg_fishtyp_id
ORDER BY ah.assethist_guid;


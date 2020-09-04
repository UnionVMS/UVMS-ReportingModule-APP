DO $$
DECLARE 
   row_offset integer := 0;
   row_batch integer := 500;
   total_rows integer := row_batch;
   begin
   WHILE total_rows = row_batch loop
		insert into reporting.asset (asset_hist_guid,asset_guid,asset_hist_active,uvi,iccat,cfr,ircs,name,ext_mark,gfcm,country_code,length_overall,main_gear_type)
		select ah.assethist_id,aa.asset_guid,ah.assethist_active,ah.assethist_uvi,ah.assethist_iccat,ah.assethist_cfr,ah.assethist_ircs,ah.assethist_nameofasset,ah.assethist_externalmarking,ah.assethist_gfcm,ah.assethist_countryregistration,ah.assethist_loa,fgt.fishgtyp_desc
		from asset.assethistory ah
		inner join asset.asset aa on aa.asset_id = ah.assethist_asset_id
		left join asset.fishinggear fg on fg.fishg_id = ah.assethist_main_fishgear_id
		left join asset.fishinggeartype fgt on fgt.fishgtyp_id = fg.fishg_fishtyp_id
		order by ah.assethist_guid
		limit row_batch offset row_offset;
		row_offset := row_offset + row_batch;
   		GET DIAGNOSTICS total_rows := ROW_COUNT;
  END LOOP;
END$$;
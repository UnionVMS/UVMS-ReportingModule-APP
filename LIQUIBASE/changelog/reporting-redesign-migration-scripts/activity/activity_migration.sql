DO $$
DECLARE 
   row_offset integer := 0;
   row_batch integer := 500;
   total_rows integer := row_batch;
   begin
   WHILE total_rows = row_batch loop
			insert into reporting.activity(id,act_id,fa_report_id,trip_id,report_type,activity_type,purpose_code,"source",accepted_timestamp,calculated_timestamp,occurrence_timestamp,start_timestamp,end_timestamp,status,is_latest,activity_coordinates,master,reason_code,asset_hist_guid)
			select fa.id,fa.id,fa.fa_report_document_id,fti.trip_id,fti.trip_scheme_id,fa.type_code,frt.purpose_code,sc.vessel_id,frd.accepted_datetime, frd.accepted_datetime,fa.occurence,dp.start_date,dp.end_date,null,fa.latest,fa.geom,vt.name,fa.reason_code,vt.guid
			from activity.activity_fishing_activity fa
			left join activity.activity_fishing_trip ft on ft.fishing_activity_id = fa.id
			left join activity.activity_fishing_trip_identifier fti on ft.id = fti.fishing_trip_id and fti.trip_scheme_id = 'EU_TRIP_ID'
			left join activity.activity_fa_report_document frd on fa.fa_report_document_id = frd.id
			left join activity.activity_flux_report_document frt on frt.id = frd.flux_report_document_id
			left join activity.activity_vessel_storage_characteristics sc on sc.id = fa.source_vessel_char_id
			left join activity.activity_delimited_period dp on dp.fishing_activity_id =fa.id
			left join activity.activity_vessel_transport_means vt on vt.fa_report_document_id = fa.fa_report_document_id
			where vt.fishing_activity_id is null
			order by fa.id
			limit row_batch offset row_offset;
		row_offset := row_offset + row_batch;
   		GET DIAGNOSTICS total_rows := ROW_COUNT;
   	raise notice '%', total_rows;
  END LOOP;
END$$;


DO $$
DECLARE 
   row_offset integer := 0;
   row_batch integer := 500;
   total_rows integer := row_batch;
   begin
   WHILE total_rows = row_batch loop
			insert into reporting.activity_catch(id,activity_id,species_code,gear_code,catch_type,weight_measure,weight_measure_unit_code,size_class,size_category,quantity)
			select distinct fc.id,fc.fishing_activity_id,fc.species_code,fc.gear_type_code,fc.species_code,fc.calculated_weight_measure,fc.weight_measure_unit_code,asdc.class_code,asd.category_code,aap.calculated_unit_quantity
			from activity.activity_fa_catch fc
			left join activity.activity_aap_process ape on ape.fa_catch_id = fc.id
			left join activity.activity_aap_process_code apec on apec.aap_process_id = ape.id and apec.type_code_list_id = 'FISH_PRESERVATION'
			left join activity.activity_aap_product aap on aap.aap_process_id = ape.id
			left join activity.activity_size_distribution asd on asd.id = fc.size_distribution_id
			left join activity.activity_size_distribution_classcode asdc on asdc.size_distribution_id = asd.id
			order by fc.id
			limit row_batch offset row_offset;
		row_offset := row_offset + row_batch;
   		GET DIAGNOSTICS total_rows := ROW_COUNT;
   	raise notice '%', total_rows;
  END LOOP;
END$$;



DO $$
DECLARE 
   row_offset integer := 0;
   row_batch integer := 500;
   total_rows integer := row_batch;
   begin
   WHILE total_rows = row_batch loop
			insert into reporting.activity_catch_location(id,activity_catch_id,catch_location_type,catch_location_type_code,catch_location_code,catch_latitude,catch_longitude)
			select fl.id,fl.fa_catch_id,fl.flux_location_identifier_scheme_id,fl.type_code,fl.flux_location_identifier,fl.latitude,fl.longitude
			from activity.activity_fa_catch fc
			inner join activity.activity_flux_location fl on fl.fa_catch_id = fc.id
			order by fl.id
			limit row_batch offset row_offset;
		row_offset := row_offset + row_batch;
   		GET DIAGNOSTICS total_rows := ROW_COUNT;
   	raise notice '%', total_rows;
  END LOOP;
END$$;


DO $$
DECLARE 
   row_offset integer := 0;
   row_batch integer := 500;
   total_rows integer := row_batch;
   begin
   WHILE total_rows = row_batch loop
			insert into reporting.activity_catch_processing(id,activity_catch_id,cf,presentation,preservation,product_weight_measure,product_weight_measure_unit_code,product_quantity)
			select aap.id,fa.id,aap.conversion_factor,aapc.type_code,aapce.type_code,aapr.weight_measure,aapr.weight_measure_unit_code,aapr.packaging_unit_count
			from activity.activity_fa_catch fa
			inner join activity.activity_aap_process aap on aap.fa_catch_id = fa.id
			left join activity.activity_aap_process_code aapc on aapc.aap_process_id = aap.id and aapc.type_code_list_id = 'FISH_PRESENTATION'
			left join activity.activity_aap_process_code aapce on aapce.aap_process_id = aap.id and aapce.type_code_list_id = 'FISH_PRESERVATION'
			left join activity.activity_aap_product aapr on aapr.aap_process_id = aap.id
			order by aap.id
			limit row_batch offset row_offset;
		row_offset := row_offset + row_batch;
   		GET DIAGNOSTICS total_rows := ROW_COUNT;
   	raise notice '%', total_rows;
  END LOOP;
END$$;



DO $$
DECLARE 
   row_offset integer := 0;
   row_batch integer := 500;
   total_rows integer := row_batch;
   begin
   WHILE total_rows = row_batch loop
			insert into reporting.activity_gear (activity_id,gear_code)
			select distinct fg.fishing_activity_id,fg.type_code
			from activity.activity_fishing_gear fg
			where fishing_activity_id is not null
			order by fg.fishing_activity_id
			limit row_batch offset row_offset;
		row_offset := row_offset + row_batch;
   		GET DIAGNOSTICS total_rows := ROW_COUNT;
   	raise notice '%', total_rows;
  END LOOP;
END$$;

DO $$
DECLARE
   row_offset integer := 0;
   row_batch integer := 500;
   total_rows integer := row_batch;
   begin
   WHILE total_rows = row_batch loop
			insert into reporting.activity_location(id,activity_id,"type",type_code,code,latitude,longitude)
			select fl.id,fl.fishing_activity_id,fl.flux_location_identifier_scheme_id,fl.type_code,fl.flux_location_identifier,fl.latitude,fl.longitude
			from activity.activity_flux_location fl
			where fl.flux_location_type = 'fa_related'
			order by fl.id
			limit row_batch offset row_offset;
		row_offset := row_offset + row_batch;
   		GET DIAGNOSTICS total_rows := ROW_COUNT;
   	raise notice '%', total_rows;
  END LOOP;
END$$;

INSERT INTO reporting.activity(id, act_id, fa_report_id, trip_id, report_type, activity_type, purpose_code, "source", accepted_timestamp, calculated_timestamp, occurrence_timestamp, start_timestamp, end_timestamp, status, is_latest, activity_coordinates, master, reason_code, asset_hist_guid)
SELECT fa.id,
       fa.id,
       afri.flux_report_identifier_id,
       fti.trip_id,
       fti.trip_scheme_id,
       fa.type_code,
       frt.purpose_code,
       sc.vessel_id,
       frd.accepted_datetime,
       frd.accepted_datetime,
       fa.occurence,
       dp.start_date,
       dp.end_date,
       NULL,
       fa.latest,
       fa.geom,
       vt.name,
       fa.reason_code,
       vt.guid
FROM activity.activity_fishing_activity fa
LEFT JOIN activity.activity_fishing_trip ft ON ft.fishing_activity_id = fa.id
LEFT JOIN activity.activity_fishing_trip_identifier fti ON ft.id = fti.fishing_trip_id
AND fti.trip_scheme_id = 'EU_TRIP_ID'
LEFT JOIN activity.activity_fa_report_document frd ON fa.fa_report_document_id = frd.id
LEFT JOIN activity.activity_flux_report_document frt ON frt.id = frd.flux_report_document_id
LEFT JOIN activity.activity_flux_report_identifier afri ON afri.id = frd.flux_report_document_id
LEFT JOIN activity.activity_vessel_storage_characteristics sc ON sc.id = fa.source_vessel_char_id
LEFT JOIN activity.activity_delimited_period dp ON dp.fishing_activity_id =fa.id
LEFT JOIN activity.activity_vessel_transport_means vt ON vt.fa_report_document_id = fa.fa_report_document_id
WHERE vt.fishing_activity_id IS NULL
ORDER BY fa.id;


INSERT INTO reporting.activity_catch(id, activity_id, species_code, gear_code, catch_type, weight_measure, weight_measure_unit_code, size_class, size_category, quantity)
SELECT DISTINCT fc.id,
                fc.fishing_activity_id,
                fc.species_code,
                fc.gear_type_code,
                fc.species_code,
                fc.calculated_weight_measure,
                fc.weight_measure_unit_code,
                asdc.class_code,
                asd.category_code,
                aap.calculated_unit_quantity
FROM activity.activity_fa_catch fc
LEFT JOIN activity.activity_aap_process ape ON ape.fa_catch_id = fc.id
LEFT JOIN activity.activity_aap_process_code apec ON apec.aap_process_id = ape.id
AND apec.type_code_list_id = 'FISH_PRESERVATION'
LEFT JOIN activity.activity_aap_product aap ON aap.aap_process_id = ape.id
LEFT JOIN activity.activity_size_distribution asd ON asd.id = fc.size_distribution_id
LEFT JOIN activity.activity_size_distribution_classcode asdc ON asdc.size_distribution_id = asd.id
ORDER BY fc.id;


INSERT INTO reporting.activity_catch_location(id, activity_catch_id, catch_location_type, catch_location_type_code, catch_location_code, catch_latitude, catch_longitude)
SELECT fl.id,
       fl.fa_catch_id,
       fl.flux_location_identifier_scheme_id,
       fl.type_code,
       fl.flux_location_identifier,
       fl.latitude,
       fl.longitude
FROM activity.activity_fa_catch fc
INNER JOIN activity.activity_flux_location fl ON fl.fa_catch_id = fc.id
ORDER BY fl.id;


INSERT INTO reporting.activity_catch_processing(id, activity_catch_id, cf, presentation, preservation, product_weight_measure, product_weight_measure_unit_code, product_quantity)
SELECT aap.id,
       fa.id,
       aap.conversion_factor,
       aapc.type_code,
       aapce.type_code,
       aapr.weight_measure,
       aapr.weight_measure_unit_code,
       aapr.packaging_unit_count
FROM activity.activity_fa_catch fa
INNER JOIN activity.activity_aap_process aap ON aap.fa_catch_id = fa.id
LEFT JOIN activity.activity_aap_process_code aapc ON aapc.aap_process_id = aap.id
AND aapc.type_code_list_id = 'FISH_PRESENTATION'
LEFT JOIN activity.activity_aap_process_code aapce ON aapce.aap_process_id = aap.id
AND aapce.type_code_list_id = 'FISH_PRESERVATION'
LEFT JOIN activity.activity_aap_product aapr ON aapr.aap_process_id = aap.id
ORDER BY aap.id;


INSERT INTO reporting.activity_gear (activity_id, gear_code)
SELECT DISTINCT fg.fishing_activity_id,
                fg.type_code
FROM activity.activity_fishing_gear fg
WHERE fishing_activity_id IS NOT NULL
ORDER BY fg.fishing_activity_id;


INSERT INTO reporting.activity_location(id, activity_id, "type", type_code, code, latitude, longitude)
SELECT fl.id,
       fl.fishing_activity_id,
       fl.flux_location_identifier_scheme_id,
       fl.type_code,
       fl.flux_location_identifier,
       fl.latitude,
       fl.longitude
FROM activity.activity_flux_location fl
WHERE fl.flux_location_type = 'fa_related'
ORDER BY fl.id;

